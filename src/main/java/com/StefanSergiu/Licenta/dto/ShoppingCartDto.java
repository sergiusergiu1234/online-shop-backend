package com.StefanSergiu.Licenta.dto;

import com.StefanSergiu.Licenta.dto.product.ProductDto;
import com.StefanSergiu.Licenta.entity.ShoppingCart;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Integer userId;
    private Long productSizeId;

    private String productName;
    private Long quantity;
    private byte[] productImage;
    private Float price;
    private Long stock;
    private String size;
    private ProductDto product;
    public static ShoppingCartDto from(ShoppingCart shoppingCart){
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setProductSizeId(shoppingCart.getProductSize().getProductSizeId());
        shoppingCartDto.setUserId(shoppingCart.getId().getUserId());
        shoppingCartDto.setQuantity(shoppingCart.getQuantity());
        shoppingCartDto.setPrice(shoppingCart.getPrice());
        shoppingCartDto.setProductName(shoppingCart.getProductSize().getProduct().getName());
        shoppingCartDto.setStock(shoppingCart.getProductSize().getStock());
        shoppingCartDto.setProduct(ProductDto.from(shoppingCart.getProductSize().getProduct()));
        shoppingCartDto.setSize(shoppingCart.getProductSize().getSize().getValue());
        return shoppingCartDto;
    }
    public static ShoppingCartDto from(ShoppingCart shoppingCart, byte[] productImage){
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setProductSizeId(shoppingCart.getProductSize().getProductSizeId());
        shoppingCartDto.setUserId(shoppingCart.getId().getUserId());
        shoppingCartDto.setQuantity(shoppingCart.getQuantity());
        shoppingCartDto.setPrice(shoppingCart.getPrice());
        shoppingCartDto.setProductName(shoppingCart.getProductSize().getProduct().getName());
        shoppingCartDto.setStock(shoppingCart.getProductSize().getStock());
        shoppingCartDto.setProductImage(productImage);
        shoppingCartDto.setProduct(ProductDto.from(shoppingCart.getProductSize().getProduct()));
        shoppingCartDto.setSize(shoppingCart.getProductSize().getSize().getValue());
        return shoppingCartDto;
    }
}
