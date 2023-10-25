package com.StefanSergiu.Licenta.dto.attribute;

import com.StefanSergiu.Licenta.dto.type.PlainTypeDto;
import com.StefanSergiu.Licenta.entity.Attribute;
import lombok.Data;

import java.util.Objects;

@Data
public class AttributeDto {
    private Long id;
    private String name;
    private PlainTypeDto plainTypeDto;
    public static AttributeDto from(Attribute attribute){
        AttributeDto attributeDto = new AttributeDto();
        attributeDto.setId(attribute.getId());
        attributeDto.setName(attribute.getName());
        if(Objects.nonNull(attribute.getType())){
            attributeDto.setPlainTypeDto(PlainTypeDto.from(attribute.getType()));
        }
        return attributeDto;
    }
}
