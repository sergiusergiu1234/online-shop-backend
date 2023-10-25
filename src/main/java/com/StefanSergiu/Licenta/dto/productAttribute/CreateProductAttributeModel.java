package com.StefanSergiu.Licenta.dto.productAttribute;

import lombok.Getter;


public class CreateProductAttributeModel {
    private Long productId;
    private Long attributeId;
    private String value;

    public Long getProductId() {
        return productId;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public String getValue() {
        return value;
    }
}
