

package com.StefanSergiu.Licenta.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

    String accessKey = System.getenv("S3_ACCESS_KEY");
    String secretKey = System.getenv("S3_SECRET_KEY");

    @Bean
    public AmazonS3 s3(){

        AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAR3DD5YTKLAAGGTWS","hUuRPB6DYfce9rUpXdkZbtYU/BjHYGNbxfjpqL5Q");
        return AmazonS3ClientBuilder
                .standard().withRegion("eu-north-1")
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}

