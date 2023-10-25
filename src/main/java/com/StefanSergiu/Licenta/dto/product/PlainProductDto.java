package com.StefanSergiu.Licenta.dto.product;

import com.StefanSergiu.Licenta.entity.Product;
import lombok.Data;

@Data
public class PlainProductDto {
    private String name;

    public static PlainProductDto from(Product product){
        PlainProductDto plainProductDto = new PlainProductDto();
        plainProductDto.setName(product.getName());
        return plainProductDto;
    }
}
