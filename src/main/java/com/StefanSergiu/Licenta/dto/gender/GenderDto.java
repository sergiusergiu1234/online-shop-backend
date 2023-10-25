package com.StefanSergiu.Licenta.dto.gender;

import com.StefanSergiu.Licenta.dto.product.PlainProductDto;
import com.StefanSergiu.Licenta.dto.product.ProductDto;
import com.StefanSergiu.Licenta.entity.Gender;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GenderDto {
    private Long id;
    private String name;
    private List<PlainProductDto> productDtoList = new ArrayList<>();

    public static GenderDto from(Gender gender){
        GenderDto genderDto= new GenderDto();
        genderDto.setId(gender.getId());
        genderDto.setName(gender.getName());
        genderDto.setProductDtoList(gender.getProducts().stream().map(PlainProductDto::from).collect(Collectors.toList()));
        return genderDto;
    }
}
