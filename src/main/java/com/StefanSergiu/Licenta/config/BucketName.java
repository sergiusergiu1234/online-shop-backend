package com.StefanSergiu.Licenta.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    PRODUCT_IMAGE("spring-amazon-image-storage");
    private final String bucketName;
}
