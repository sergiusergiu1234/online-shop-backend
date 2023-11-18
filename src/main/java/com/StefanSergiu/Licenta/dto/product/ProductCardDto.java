package com.StefanSergiu.Licenta.dto.product;

import com.StefanSergiu.Licenta.dto.productSize.ProductSizeDto;
import com.StefanSergiu.Licenta.dto.size.SizeDto;
import com.StefanSergiu.Licenta.entity.Product;
import com.StefanSergiu.Licenta.entity.ProductSize;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductCardDto implements ProductCardProjection{
    private Long id;
    private String name;
    private Float price;
    public static ProductCardDto from(Product product){
        ProductCardDto productCardDto = new ProductCardDto();
        productCardDto.setId(product.getId());
        productCardDto.setName(product.getName());
        productCardDto.setPrice(product.getPrice());
        return productCardDto;
    }
}
