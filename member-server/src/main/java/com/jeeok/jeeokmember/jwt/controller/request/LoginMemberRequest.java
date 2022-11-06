package com.jeeok.jeeokmember.jwt.controller.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginMemberRequest {

    private String email;
    private String password;
}
