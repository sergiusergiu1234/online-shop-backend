package com.StefanSergiu.Licenta.dto.favorite;

import com.StefanSergiu.Licenta.dto.product.ProductDto;
import com.StefanSergiu.Licenta.dto.productAttribute.PlainProductAttributeDto;
import com.StefanSergiu.Licenta.entity.Favorite;
import com.StefanSergiu.Licenta.entity.ProductAttribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FavoriteDto {
    private Integer userId;
    private Long productSizeId;
    private String productName;
    private Float price;
    private byte[] productImage;
    private String size;
    public static FavoriteDto from(Favorite favorite){
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setProductSizeId(favorite.getId().getProductSizeId());
        favoriteDto.setUserId(favorite.getUser().getId());
        favoriteDto.setProductName(favorite.getProductSize().getProduct().getName());
        favoriteDto.setPrice(favorite.getProductSize().getProduct().getPrice());
        favoriteDto.setSize(favorite.getProductSize().getSize().getValue());
        return favoriteDto;
    }
    public static FavoriteDto from(Favorite favorite, byte[]productImage){
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setProductSizeId(favorite.getId().getProductSizeId());
        favoriteDto.setUserId(favorite.getUser().getId());
        favoriteDto.setProductName(favorite.getProductSize().getProduct().getName());
        favoriteDto.setPrice(favorite.getProductSize().getProduct().getPrice());
        favoriteDto.setSize(favorite.getProductSize().getSize().getValue());
        favoriteDto.setProductImage(productImage);
        return favoriteDto;
    }
}
