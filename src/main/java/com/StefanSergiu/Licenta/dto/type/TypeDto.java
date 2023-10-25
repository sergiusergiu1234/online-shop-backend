package com.StefanSergiu.Licenta.dto.type;

import com.StefanSergiu.Licenta.dto.attribute.PlainAttributeDto;
import com.StefanSergiu.Licenta.dto.category.PlainCategoryDto;
import com.StefanSergiu.Licenta.dto.size.SizeDto;
import com.StefanSergiu.Licenta.entity.Type;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TypeDto {
    private Long id;
    private String name;
    private List<PlainCategoryDto> categoryDtoList = new ArrayList<>();

    // Updated field for attribute values
    private Map<String, List<String>> attributeValues = new HashMap<>();
    private List<PlainAttributeDto> attributeDtoList = new ArrayList<>();

    private List<SizeDto> sizeDtoList = new ArrayList<>();
    public static TypeDto from(Type type){
        TypeDto typeDto = new TypeDto();
        typeDto.setId(type.getId());
        typeDto.setName(type.getName());
        //get all categories linked to type and transform into plainDtos
        typeDto.setCategoryDtoList(type.getCategories().stream().map(PlainCategoryDto::from).collect(Collectors.toList()));
        //get all types linked to type and transform into plainDtos
        typeDto.setAttributeDtoList(type.getAttributes().stream().map(PlainAttributeDto::from).collect(Collectors.toList()));
        typeDto.setSizeDtoList(type.getSizes().stream().map(SizeDto::from).collect(Collectors.toList()));
        return typeDto;
    }
}
