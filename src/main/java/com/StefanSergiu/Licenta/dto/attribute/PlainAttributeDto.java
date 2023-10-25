package com.StefanSergiu.Licenta.dto.attribute;

import com.StefanSergiu.Licenta.entity.Attribute;
import lombok.Data;

@Data
public class PlainAttributeDto {
    private String name;
    private Long id;
    public static PlainAttributeDto from(Attribute attribute){
        PlainAttributeDto plainAttributeDto = new PlainAttributeDto();
        plainAttributeDto.setName(attribute.getName());
        plainAttributeDto.setId(attribute.getId());
        return plainAttributeDto;
    }
}
