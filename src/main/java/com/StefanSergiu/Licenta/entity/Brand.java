package com.StefanSergiu.Licenta.entity;

import com.StefanSergiu.Licenta.dto.brand.BrandDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "brand")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "brand",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public static Brand from(BrandDto brandDto){
        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        return brand;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product){
        products.remove(product);
    }
}
