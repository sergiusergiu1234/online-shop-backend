package com.StefanSergiu.Licenta.dto.productAttribute;

import com.StefanSergiu.Licenta.entity.ProductAttribute;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlainProductAttributeDto {
    private String attribute_name;
    private String value;
    public static PlainProductAttributeDto from(ProductAttribute productAttribute){
        PlainProductAttributeDto dto = new PlainProductAttributeDto();

        dto.setAttribute_name(productAttribute.getAttribute().getName());
        dto.setValue(productAttribute.getValue());

        return dto;
    }
}
