package com.StefanSergiu.Licenta.dto.product;

import com.StefanSergiu.Licenta.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductCardDto productToProductCardDto(Product product);
}
