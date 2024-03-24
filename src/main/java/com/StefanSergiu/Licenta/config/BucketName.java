package com.StefanSergiu.Licenta.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    PRODUCT_IMAGE("newbucket-slope-emporium");
    private final String bucketName;
}
