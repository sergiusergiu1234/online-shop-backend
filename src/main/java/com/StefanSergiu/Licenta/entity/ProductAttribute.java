package com.StefanSergiu.Licenta.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "product_attribute")
@Data
@NoArgsConstructor
public class ProductAttribute {
    @EmbeddedId
    private ProductAttributeKey id;

    @ManyToOne
    @MapsId("productId")
    private  Product product;

    @ManyToOne
    @MapsId("attributeId")
    private Attribute attribute;

    @Column
    String value;

}
