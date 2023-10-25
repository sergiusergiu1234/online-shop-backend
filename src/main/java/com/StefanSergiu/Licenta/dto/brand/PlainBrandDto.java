package com.StefanSergiu.Licenta.dto.brand;

import com.StefanSergiu.Licenta.entity.Brand;
import lombok.Data;

@Data
public class PlainBrandDto {
    private Long id;
    private String name;

    public static PlainBrandDto from(Brand brand){
        PlainBrandDto plainBrandDto = new PlainBrandDto();
        plainBrandDto.setId(brand.getId());
        plainBrandDto.setName(brand.getName());
        return plainBrandDto;
    }
}
