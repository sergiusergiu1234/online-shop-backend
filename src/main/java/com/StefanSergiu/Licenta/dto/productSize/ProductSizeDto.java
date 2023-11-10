package com.StefanSergiu.Licenta.dto.productSize;


import com.StefanSergiu.Licenta.dto.size.SizeDto;
import com.StefanSergiu.Licenta.entity.ProductSize;
import lombok.Data;
import lombok.Getter;

@Data
public class ProductSizeDto {
    private Long productId;
    private SizeDto size;
    private Long stock;

    public static ProductSizeDto from(ProductSize productSize){
        ProductSizeDto productSizeDto = new ProductSizeDto();

        productSizeDto.setProductId(productSize.getProduct().getId());
        productSizeDto.setSize(SizeDto.from(productSize.getSize()));
        productSizeDto.setStock(productSize.getStock());
        return productSizeDto;
    }
}
