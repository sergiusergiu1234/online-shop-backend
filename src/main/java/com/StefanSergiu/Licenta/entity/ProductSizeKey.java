package com.StefanSergiu.Licenta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSizeKey implements Serializable {
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "size_id")
    private Long sizeId;

}
