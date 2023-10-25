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
    private Long productId;
    private String productName;
    private Float price;
    private byte[] productImage;
    private String size;
    public static FavoriteDto from(Favorite favorite){
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setProductId(favorite.getId().getProductId());
        favoriteDto.setUserId(favorite.getUser().getId());
        favoriteDto.setProductName(favorite.getProduct().getName());
        favoriteDto.setPrice(favorite.getProduct().getPrice());
        favoriteDto.setSize(favorite.getProduct().getSize());
        return favoriteDto;
    }
    public static FavoriteDto from(Favorite favorite, byte[]productImage){
        FavoriteDto favoriteDto = new FavoriteDto();
        favoriteDto.setProductId(favorite.getId().getProductId());
        favoriteDto.setUserId(favorite.getUser().getId());
        favoriteDto.setProductName(favorite.getProduct().getName());
        favoriteDto.setPrice(favorite.getProduct().getPrice());
        favoriteDto.setProductImage(productImage);
        favoriteDto.setSize(favorite.getProduct().getSize());
        return favoriteDto;
    }
}
