package com.StefanSergiu.Licenta.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shopping_cart")
@Data
@NoArgsConstructor
public class ShoppingCart {
    @EmbeddedId
    private ShoppingCartKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "userId", nullable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productSizeId")
    @JoinColumn(name = "productSizeId", nullable = false)
    private ProductSize productSize;

     Long quantity;

     Float price;
}
