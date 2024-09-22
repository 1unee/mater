package com.oneune.mater.rest.main.controllers;

import com.oneune.mater.rest.main.services.SelectelS3Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("selectel-s3/objects")
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SelectelS3Controller {

    SelectelS3Service selectelS3Service;

    @GetMapping("{objectName}/url")
    public ResponseEntity<String> getObjectUrl(@PathVariable String objectName,
                                               @RequestParam(required = false, defaultValue = "false") Boolean presigned) {
        if (presigned) {
            return ResponseEntity.ok(selectelS3Service.generateObjectPresignedUrl(objectName));
        } else {
            return ResponseEntity.ok(selectelS3Service.getObjectUrl(objectName));
        }
    }

    @GetMapping("{objectName}")
    public ResponseEntity<String> downloadObject(@PathVariable String objectName,
                                                 @RequestParam String destinationFilePath) {
        try {
            selectelS3Service.downloadObject(objectName, destinationFilePath);
            return ResponseEntity.ok("Object downloaded: " + objectName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to download object: " + e.getMessage());
        }
    }

    @DeleteMapping("{objectName}")
    public ResponseEntity<String> deleteObject(@PathVariable String objectName) {
        selectelS3Service.deleteObject(objectName);
        return ResponseEntity.ok("Object deleted: " + objectName);
    }
}

