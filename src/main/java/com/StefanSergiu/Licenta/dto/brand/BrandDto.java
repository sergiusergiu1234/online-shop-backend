package com.StefanSergiu.Licenta.dto.brand;

import com.StefanSergiu.Licenta.dto.product.PlainProductDto;
import com.StefanSergiu.Licenta.dto.product.ProductDto;
import com.StefanSergiu.Licenta.entity.Brand;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class BrandDto {
    private Long id;
    private String name;
    private List<PlainProductDto> productDtoList = new ArrayList<>();
    public static BrandDto from(Brand brand){
        BrandDto brandDto = new BrandDto();
        brandDto.setId(brand.getId());
        brandDto.setName(brand.getName());
        brandDto.setProductDtoList(brand.getProducts().stream().map(PlainProductDto::from).collect(Collectors.toList()));
        return brandDto;
    }
}
