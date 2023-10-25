package com.StefanSergiu.Licenta.dto.type;

import com.StefanSergiu.Licenta.entity.Type;
import lombok.Data;

@Data
public class PlainTypeDto {
    private String name;
    public static PlainTypeDto from(Type type) {
        PlainTypeDto plainTypeDto = new PlainTypeDto();
        plainTypeDto.setName(type.getName());
        return plainTypeDto;
    }
}
