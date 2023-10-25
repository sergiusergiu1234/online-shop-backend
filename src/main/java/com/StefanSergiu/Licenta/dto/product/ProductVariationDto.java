package com.StefanSergiu.Licenta.dto.product;

import com.StefanSergiu.Licenta.dto.brand.PlainBrandDto;
import com.StefanSergiu.Licenta.dto.category.PlainCategoryDto;
import com.StefanSergiu.Licenta.dto.gender.PlainGenderDto;
import com.StefanSergiu.Licenta.dto.productAttribute.PlainProductAttributeDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductVariationDto {
    private Long id;
    private String size;
    private boolean isFavorite;
    private Long stock;
}
