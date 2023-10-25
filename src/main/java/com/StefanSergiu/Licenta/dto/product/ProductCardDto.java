package com.StefanSergiu.Licenta.dto.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductCardDto {
    private String name;
    private Float price;
    private String description;
    private byte[] image;
    private String gender;
    private String category;
    private String brand;
    private List<ProductVariationDto> sizes;
}
