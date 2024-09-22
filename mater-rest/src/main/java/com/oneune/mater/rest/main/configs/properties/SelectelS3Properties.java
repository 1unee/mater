package com.oneune.mater.rest.main.configs.properties;

import com.oneune.mater.rest.common.aop.annotations.ConfigurationPropertiesInfo;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.net.URL;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "selectel.s3")
@PropertySource("classpath:application.yml")
@ConfigurationPropertiesInfo
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class SelectelS3Properties {

    Credentials credentials;
    BucketProperties bucketProperties;
    ObjectProperties objectProperties;

    @PostConstruct
    public void init() {
        if (Objects.isNull(credentials)
                || Objects.isNull(credentials.getAccessKey())
                || Objects.isNull(credentials.getSecretKey())
                || Objects.isNull(credentials.getUrl())
                || Objects.isNull(bucketProperties)
                || Objects.isNull(bucketProperties.getName())
                || Objects.isNull(bucketProperties.getDomain())) {
            throw new IllegalStateException("""
                You need to specify selectel s3 configuration properties.
                """);
        }
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    public static class Credentials {
        String accessKey;
        String secretKey;
        URL url;
        String region = "ru-1";
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    public static class BucketProperties {
        String name;
        String domain;
    }

    @FieldDefaults(level = AccessLevel.PRIVATE)
    @Data
    public static class ObjectProperties {
        Duration urlExpiration = Duration.of(1, TimeUnit.DAYS.toChronoUnit());
    }
}
