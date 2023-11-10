package com.StefanSergiu.Licenta.entity;

import com.StefanSergiu.Licenta.dto.product.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

//import java.io.Serial;
import java.io.Serializable;
import java.util.*;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private Float price;
    @ManyToOne
    @JoinColumn(name = "brand_id",nullable = false)
    private Brand brand;
    @ManyToOne
    @JoinColumn(name = "gender_id",nullable = false)
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;
    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductAttribute> productAttributes=new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductSize> productSizes= new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();
    private String imagePath;
    private String imageFileName;
    @Column(length = 5000)
    private String description;

   @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ShoppingCart>shoppingCarts = new ArrayList<>();

   private Long stock;

}
