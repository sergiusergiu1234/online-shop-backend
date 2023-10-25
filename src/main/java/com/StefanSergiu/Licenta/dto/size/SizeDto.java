package com.StefanSergiu.Licenta.dto.size;

import com.StefanSergiu.Licenta.entity.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SizeDto {
    private Long id;
    private String value;
    private Long typeId;

 public static SizeDto from(Size size){
     SizeDto sizeDto = new SizeDto();
     sizeDto.setId(size.getId());
     sizeDto.setValue(size.getValue());
     sizeDto.setTypeId(size.getType().getId());
     return sizeDto;
 }
}
