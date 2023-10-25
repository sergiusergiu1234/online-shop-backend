package com.StefanSergiu.Licenta.dto.gender;


import com.StefanSergiu.Licenta.entity.Gender;
import lombok.Data;

@Data
public class PlainGenderDto {
    private Long id;
    private String name;

    public static PlainGenderDto from(Gender gender){
        PlainGenderDto plainGenderDto = new PlainGenderDto();
        plainGenderDto.setId(gender.getId());
        plainGenderDto.setName(gender.getName());
        return plainGenderDto;
    }
}
