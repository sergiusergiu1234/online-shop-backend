package com.StefanSergiu.Licenta.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="favorite")
@Data
@NoArgsConstructor
public class Favorite {
    @EmbeddedId
    private FavoriteKey id;

    @ManyToOne( fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "userId",nullable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "productId",nullable = false)
    private Product product;


}
