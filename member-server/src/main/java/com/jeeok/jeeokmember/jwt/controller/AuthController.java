package com.jeeok.jeeokmember.jwt.controller;

import com.jeeok.jeeokmember.common.json.JsonResult;
import com.jeeok.jeeokmember.common.utils.CookieProvider;
import com.jeeok.jeeokmember.jwt.controller.response.RefreshTokenResponse;
import com.jeeok.jeeokmember.jwt.dto.JwtTokenDto;
import com.jeeok.jeeokmember.jwt.service.AccessTokenService;
import com.jeeok.jeeokmember.jwt.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final CookieProvider cookieProvider;

    @PostMapping("/login")
    public ResponseEntity<JsonResult> login() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonResult.OK("okokok"));
    }

    @GetMapping("/reissue")
    public ResponseEntity<JsonResult> refreshToken(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                                   @CookieValue(value = "refresh-token") String refreshToken) {

        JwtTokenDto jwtTokenDto = refreshTokenService.refreshJwtToken(accessToken, refreshToken);

        ResponseCookie responseCookie = cookieProvider.createRefreshTokenCookie(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(JsonResult.OK(new RefreshTokenResponse(jwtTokenDto)));
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonResult> logout(@RequestHeader("X-AUTH-TOKEN") String accessToken) {

        refreshTokenService.logoutToken(accessToken);

        ResponseCookie responseCookie = cookieProvider.removeRefreshTokenCookie();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(JsonResult.OK());
    }

    @GetMapping("/check/access-token")
    public ResponseEntity<JsonResult> checkAccessToken(@RequestHeader(name = "Authorization") String authrization) {

        accessTokenService.checkAccessToken(authrization);

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonResult.OK());
    }
}
