package com.StefanSergiu.Licenta.dto.product;

import com.StefanSergiu.Licenta.dto.productSize.ProductSizeDto;
import com.StefanSergiu.Licenta.dto.size.SizeDto;
import com.StefanSergiu.Licenta.entity.Product;
import com.StefanSergiu.Licenta.entity.ProductSize;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductCardDto {
    private Long id;
    private String name;
    private Float price;
    private byte[] image;
    private List<SizeDto> sizes;
    public ProductCardDto() {
        this.sizes = new ArrayList<>();
    }
    public static ProductCardDto from(Product product){
        ProductCardDto productCardDto = new ProductCardDto();
        productCardDto.setId(product.getId());
        productCardDto.setName(product.getName());
        productCardDto.setPrice(product.getPrice());
        for(ProductSize size : product.getProductSizes()){
            productCardDto.sizes.add(SizeDto.fromProductSize(size));
        }

        //image is set in service
        return productCardDto;
    }
}
