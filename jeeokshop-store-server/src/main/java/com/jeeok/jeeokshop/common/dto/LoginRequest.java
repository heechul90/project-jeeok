package com.jeeok.jeeokshop.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    private String email;
    private String password;
    private String name;
}
