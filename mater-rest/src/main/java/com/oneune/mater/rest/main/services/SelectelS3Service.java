package com.oneune.mater.rest.main.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.oneune.mater.rest.main.configs.properties.SelectelS3Properties;
import com.oneune.mater.rest.main.utils.ImageUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static com.oneune.mater.rest.main.utils.ImageUtils.DEFAULT_COMPRESS_MULTIPLIER;
import static com.oneune.mater.rest.main.utils.ImageUtils.DEFAULT_QUALITY;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class SelectelS3Service {

    SelectelS3Properties selectelS3Properties;
    AmazonS3 s3Client;
    Queue<List<MultipartFile>> multipartFilesQueue = new ConcurrentLinkedQueue<>();

    @NonFinal
    volatile Boolean isMultipartFileProcessing = false;

    public boolean isFileExist(String fileName) {
        return s3Client.doesObjectExist(selectelS3Properties.getBucketProperties().getName(), fileName);
    }

    private byte[] getPreparedFileContent(MultipartFile file) {
        if (Objects.nonNull(file.getContentType()) && file.getContentType().startsWith("image")) {
            return ImageUtils.compressImage(file, DEFAULT_QUALITY, DEFAULT_COMPRESS_MULTIPLIER);
        } else {
            try(InputStream inputStream = file.getInputStream()) {
                return inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void uploadFile(MultipartFile file) {
        byte[] preparedFileContent = getPreparedFileContent(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(preparedFileContent.length);
        metadata.setContentType(file.getContentType());
        s3Client.putObject(new PutObjectRequest(
                selectelS3Properties.getBucketProperties().getName(),
                file.getOriginalFilename(), new ByteArrayInputStream(preparedFileContent), metadata
        ));
    }

    /**
     * Does not create duplicates.
     */
    public void uploadFiles() {
        if (!multipartFilesQueue.isEmpty()) {
            isMultipartFileProcessing = true;
            multipartFilesQueue.poll().stream()
                    .filter(file -> !isFileExist(file.getOriginalFilename()))
                    .forEach(this::uploadFile);
            isMultipartFileProcessing = false;
        }
    }

    public void uploadFiles(List<MultipartFile> files) {
        multipartFilesQueue.add(files);
        log.info("File queue size is {}", multipartFilesQueue.size());
        while (isMultipartFileProcessing) { Thread.onSpinWait(); }
        uploadFiles();
    }

    public List<String> getFilenames() {
        return s3Client
                .listObjects(selectelS3Properties.getBucketProperties().getName())
                .getObjectSummaries()
                .stream()
                .map(S3ObjectSummary::getKey)
                .toList();
    }

    public String getObjectUrl(String objectName) {
        return String.format("%s/%s", selectelS3Properties.getBucketProperties().getDomain(), objectName);
    }

    public Map<String, Pair<String, Long>> getFilesMeta(List<String> filenames) {
        return s3Client.listObjects(selectelS3Properties.getBucketProperties().getName())
                .getObjectSummaries()
                .stream()
                .filter(obj -> filenames.contains(obj.getKey()))
                .collect(Collectors.toMap(
                S3ObjectSummary::getKey,
                obj -> Pair.of(getObjectUrl(obj.getKey()), obj.getSize())
        ));
    }

    public String generateObjectPresignedUrl(String objectName) {
        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
                selectelS3Properties.getBucketProperties().getName(),
                objectName
        )
                .withMethod(HttpMethod.GET)
                .withExpiration(new Date(selectelS3Properties.getObjectProperties().getUrlExpiration().toMillis()));
        URL url = s3Client.generatePresignedUrl(urlRequest);
        return url.toString();
    }

    public void downloadObject(String objectName,
                               String destinationFilePath) throws IOException {
        S3Object s3Object = s3Client.getObject(selectelS3Properties.getBucketProperties().getName(), objectName);
        try (S3ObjectInputStream inputStream = s3Object.getObjectContent();
             FileOutputStream fileOutputStream = new FileOutputStream(destinationFilePath)) {
            inputStream.transferTo(fileOutputStream);
        }
    }

    public void deleteObject(String objectName) {
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(
                selectelS3Properties.getBucketProperties().getName(),
                objectName
        );
        s3Client.deleteObject(deleteObjectRequest);
    }

    public void deleteObjects(List<String> objectNames) {
        List<KeyVersion> objectsToDelete = objectNames.stream().map(KeyVersion::new).toList();
        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(
                selectelS3Properties.getBucketProperties().getName()
        ).withKeys(objectsToDelete);
        s3Client.deleteObjects(deleteObjectsRequest);
    }
}

