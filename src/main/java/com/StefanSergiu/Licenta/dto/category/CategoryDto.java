package com.StefanSergiu.Licenta.dto.category;

import com.StefanSergiu.Licenta.dto.brand.PlainBrandDto;
import com.StefanSergiu.Licenta.dto.product.PlainProductDto;
import com.StefanSergiu.Licenta.dto.type.PlainTypeDto;
import com.StefanSergiu.Licenta.entity.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String typeName;

    public static CategoryDto from(Category category){
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setTypeName(category.getType().getName());
        return categoryDto;
    }
}
