package com.StefanSergiu.Licenta.dto.size;

import com.StefanSergiu.Licenta.entity.ProductSize;
import com.StefanSergiu.Licenta.entity.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SizeDto {
    private Long id;
    private String value;

public static SizeDto fromProductSize(ProductSize productSize){
    SizeDto sizeDto = new SizeDto();
    sizeDto.setId(productSize.getSize().getId());
    sizeDto.setValue(productSize.getSize().getValue());
    return sizeDto;
}
 public static SizeDto from(Size size){
     SizeDto sizeDto = new SizeDto();
     sizeDto.setId(size.getId());
     sizeDto.setValue(size.getValue());

     return sizeDto;
 }
}
