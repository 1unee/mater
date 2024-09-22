package com.oneune.mater.rest.main.configs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.oneune.mater.rest.common.aop.annotations.ConfigurationBeansInfo;
import com.oneune.mater.rest.main.configs.properties.SelectelS3Properties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SelectelS3Properties.class)
@ConfigurationBeansInfo
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SelectelS3Config {

    SelectelS3Properties selectelS3Properties;

    private AWSCredentials getAwsCredentials() {
        return new BasicAWSCredentials(
                selectelS3Properties.getCredentials().getAccessKey(),
                selectelS3Properties.getCredentials().getSecretKey()
        );
    }

    private EndpointConfiguration getEndpointConfig() {
        return new EndpointConfiguration(
                selectelS3Properties.getCredentials().getUrl().toString(),
                selectelS3Properties.getCredentials().getRegion()
        );
    }

    @Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = getAwsCredentials();
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        EndpointConfiguration endpointConfig = getEndpointConfig();
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withPathStyleAccessEnabled(true)
                .withEndpointConfiguration(endpointConfig)
                .build();
    }
}
