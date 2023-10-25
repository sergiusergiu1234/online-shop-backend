package com.StefanSergiu.Licenta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeKey implements Serializable {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "attribute_id")
    private Long attributeId;
}