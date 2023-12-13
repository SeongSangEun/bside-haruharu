package com.bigbang.haruharu.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${cloud.ncp.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.ncp.credentials.secretKey}")
    private String secretKey;
    private final String endPoint = "https://kr.object.ncloudstorage.com";
    private final String regionName = "kr-standard";
    private String region = "kr-standard";

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        InstanceProfileCredentialsProvider.getInstance();


        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

}
