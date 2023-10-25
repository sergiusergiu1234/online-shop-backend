package com.StefanSergiu.Licenta.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String token;
    private String role;


}
