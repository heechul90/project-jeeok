package com.jeeok.jeeokmember.jwt.controller.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignupMemberResponse {

    private Long signupMemberId;
}
