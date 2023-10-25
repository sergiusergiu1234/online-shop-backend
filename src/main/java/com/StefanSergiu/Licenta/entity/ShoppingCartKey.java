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
public class ShoppingCartKey implements Serializable {
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "product_id")
    private Long productId;
}
