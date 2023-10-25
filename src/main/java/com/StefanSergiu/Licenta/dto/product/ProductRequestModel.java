package com.StefanSergiu.Licenta.dto.product;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestModel {
    private String name;
    private String brands;
    private String category_name;
    private String genders;
    private Float price;
    private Float minPrice;
    private Float maxPrice;
    private String type_name;
    private Map<String, String>  attributes;
    private String sizes;

}
