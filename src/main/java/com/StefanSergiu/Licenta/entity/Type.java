package com.StefanSergiu.Licenta.entity;

import com.StefanSergiu.Licenta.dto.type.TypeDto;
import jakarta.persistence.*;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "type",cascade =CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();


    @OneToMany(mappedBy = "type",cascade = CascadeType.ALL, fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Attribute> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "type",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Size> sizes = new ArrayList<>();

    public static Type from(TypeDto typeDto){
        Type type = new Type();
        type.setName(typeDto.getName());
        return type;
    }
    public void addCategory(Category category){categories.add(category);}
    public void removeCategory(Category category){categories.remove(category);}

    public void addAttribute(Attribute attribute){attributes.add(attribute);}
    public void addSize(Size size){sizes.add(size);}
    public void removeAttribute(Attribute attribute){attributes.remove(attribute);}


}
