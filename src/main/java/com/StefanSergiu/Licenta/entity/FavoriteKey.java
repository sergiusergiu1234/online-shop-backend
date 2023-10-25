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
public class FavoriteKey implements Serializable {
    @Column(name = "user_id",nullable = false)
    private Integer userId;

    @Column(name = "product_id",nullable = false)
    private Long productId;
}
