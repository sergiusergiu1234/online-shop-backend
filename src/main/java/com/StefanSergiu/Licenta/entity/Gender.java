package com.StefanSergiu.Licenta.entity;


import com.StefanSergiu.Licenta.dto.gender.GenderDto;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "gender")
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy ="gender",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    public static Gender from(GenderDto genderDto){
        Gender gender = new Gender();
        gender.setName(genderDto.getName());
        return gender;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product){
        products.remove(product);
    }
}
