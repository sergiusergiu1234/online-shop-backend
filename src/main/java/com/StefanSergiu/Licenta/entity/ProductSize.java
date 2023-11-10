package com.StefanSergiu.Licenta.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_size")
@Data
@NoArgsConstructor
public class ProductSize {
    @EmbeddedId
    private ProductSizeKey id;
    @ManyToOne
    @MapsId("productId")
    private  Product product;

    @ManyToOne
    @MapsId("attributeId")
    private Size size;

    private Long stock;

}
