package com.StefanSergiu.Licenta.entity;

import com.StefanSergiu.Licenta.dto.category.CategoryDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false,unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id",nullable = false)
    @JsonIgnoreProperties("categories")
    private Type type;

    @OneToMany(mappedBy = "category",cascade =CascadeType.ALL, orphanRemoval = true)
    private List<Product>products = new ArrayList<>();

    public static Category from(CategoryDto categoryDto){
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public void addProduct(Product product){products.add(product);}
    public void removeProduct(Product product){products.remove(product);}
}
