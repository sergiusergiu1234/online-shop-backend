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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false)
    private UserInfo user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productSize_id", nullable = false, insertable = false, updatable = false)
    private ProductSize productSize;


}
