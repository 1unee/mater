package com.oneune.mater.rest.main.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.oneune.mater.rest.main.configs.properties.SelectelS3Properties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class SelectelS3Service {

    AmazonS3 s3Client;
    SelectelS3Properties selectelS3Properties;

    public boolean isFileExist(String fileName) {
        return s3Client.doesObjectExist(selectelS3Properties.getBucketProperties().getName(), fileName);
    }

    public Pair<String, String> uploadFile(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        try (InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(
                    selectelS3Properties.getBucketProperties().getName(),
                    file.getOriginalFilename(),
                    inputStream,
                    metadata
            ));
        } catch (IOException e) {
            log.error(e);
        }
        return Pair.of(
                Optional.ofNullable(file.getOriginalFilename()).orElseThrow(),
                getObjectUrl(file.getOriginalFilename())
        );
    }

    /**
     * Does not create duplicates.
     */
    public Map<String, String> uploadFiles(List<MultipartFile> files) {
        return files.stream()
                .filter(file -> !isFileExist(file.getOriginalFilename()))
                .map(this::uploadFile)
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond, (l, r) -> l));
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

    public Map<String, String> getFileUrls(List<String> filenames) {
        return s3Client.listObjects(selectelS3Properties.getBucketProperties().getName())
                .getObjectSummaries()
                .stream()
                .filter(obj -> filenames.contains(obj.getKey()))
                .collect(Collectors.toMap(S3ObjectSummary::getKey, obj -> getObjectUrl(obj.getKey())));
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
        s3Client.deleteObject(selectelS3Properties.getBucketProperties().getName(), objectName);
    }

    public void deleteObjects(List<String> objectNames) {
        objectNames.forEach(this::deleteObject);
    }
}

