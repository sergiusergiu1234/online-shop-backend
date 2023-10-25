package com.StefanSergiu.Licenta.dto;

import com.StefanSergiu.Licenta.dto.product.ProductDto;
import com.StefanSergiu.Licenta.entity.ShoppingCart;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Integer userId;
    private Long productId;

    private String productName;
    private Long quantity;
    private byte[] productImage;
    private Float price;
    private ProductDto product;
    private Long stock;

    public static ShoppingCartDto from(ShoppingCart shoppingCart){
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setProductId(shoppingCart.getId().getProductId());
        shoppingCartDto.setUserId(shoppingCart.getId().getUserId());
        shoppingCartDto.setQuantity(shoppingCart.getQuantity());
        shoppingCartDto.setPrice(shoppingCart.getPrice());
        shoppingCartDto.setProductName(shoppingCart.getProduct().getName());
        shoppingCartDto.setProduct(ProductDto.from(shoppingCart.getProduct()));
        shoppingCartDto.setStock(shoppingCart.getProduct().getStock());
        return shoppingCartDto;
    }
    public static ShoppingCartDto from(ShoppingCart shoppingCart, byte[] productImage){
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setProductId(shoppingCart.getId().getProductId());
        shoppingCartDto.setUserId(shoppingCart.getId().getUserId());
        shoppingCartDto.setQuantity(shoppingCart.getQuantity());
        shoppingCartDto.setPrice(shoppingCart.getPrice());
        shoppingCartDto.setProductImage(productImage);
        shoppingCartDto.setProductName(shoppingCart.getProduct().getName());
        shoppingCartDto.setProduct(ProductDto.from(shoppingCart.getProduct()));
        return shoppingCartDto;
    }
}
