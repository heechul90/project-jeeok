package com.jeeok.jeeokmember.jwt.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtTokenDto {

    private String accessToken;
    private String refreshToken;
    private Date accessTokenExpiredDate;

    @Builder
    public JwtTokenDto(String accessToken, String refreshToken, Date accessTokenExpiredDate) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiredDate = accessTokenExpiredDate;
    }
}
