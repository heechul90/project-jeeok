package com.jeeok.jeeokmember.jwt.controller.response;

import com.jeeok.jeeokmember.jwt.dto.JwtTokenDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenResponse {

    private String accessToken;
    private String expiredTime;

    public RefreshTokenResponse(JwtTokenDto jwtTokenDto) {
        this.accessToken = jwtTokenDto.getAccessToken();
        this.expiredTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jwtTokenDto.getAccessTokenExpiredDate());
    }
}
