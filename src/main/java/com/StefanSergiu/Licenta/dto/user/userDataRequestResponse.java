package com.StefanSergiu.Licenta.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userDataRequestResponse {
    private String name;
    private String email;
    private Long mobile;
    private String address;

    //TODO    private String orders;

}
