package com.spring.app.aws.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3Properties {

    private String bucketName;
}
