package com.jeeok.jeeokmember.core.auth.service;

import com.jeeok.jeeokmember.common.utils.JwtTokenProvider;
import com.jeeok.jeeokmember.core.auth.exception.AccessTokenNotValidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final JwtTokenProvider jwtTokenProvider;

    public void checkAccessToken(String authorization) {
        String token = authorization.replace("Bearer ", "");
        if (!jwtTokenProvider.validateJwtToken(token)) {
            throw new AccessTokenNotValidException("Access token is not valid.");
        }
    }
}
