package com.StefanSergiu.Licenta.dto.productAttribute;

import com.StefanSergiu.Licenta.entity.ProductAttribute;
import lombok.Data;

@Data
public class ProductAttributeDto {
    private Long productId;
    private Long attributeId;
    private String attributeName;
    private String value;
    public static ProductAttributeDto from(ProductAttribute productAttribute){
        ProductAttributeDto productAttributeDto = new ProductAttributeDto();

        productAttributeDto.setProductId(productAttribute.getProduct().getId());
        productAttributeDto.setAttributeId(productAttribute.getAttribute().getId());
        productAttributeDto.setAttributeName(productAttribute.getAttribute().getName());
        productAttributeDto.setValue(productAttribute.getValue());

        return productAttributeDto;
    }
}
