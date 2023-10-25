package com.StefanSergiu.Licenta.dto.product;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CreateNewProductModel {
    private String name;
    private String size;
    private String brand_name;
    private String gender_name;
    private String category_name;
    private Float price;
    private String description;
    private Long stock;

}
