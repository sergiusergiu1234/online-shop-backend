package com.StefanSergiu.Licenta.dto.user;

import com.StefanSergiu.Licenta.entity.UserInfo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;


    public static UserDto from(UserInfo userInfo){
        UserDto userDto = new UserDto();
        userDto.setId(userInfo.getId());
        userDto.setUsername(userInfo.getUsername());
        userDto.setEmail(userInfo.getEmail());
        userDto.setFirstName(userInfo.getFirstName());
        userDto.setLastName(userInfo.getLastName());
        userDto.setPassword(userInfo.getPassword());
        userDto.setPhoneNumber(userInfo.getPhoneNumber());
        return userDto;
    }
}
