package com.jeeok.jeeokmember.core.auth.controller;

import com.jeeok.jeeokmember.common.json.JsonResult;
import com.jeeok.jeeokmember.common.utils.CookieProvider;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.auth.controller.request.SignupMemberRequest;
import com.jeeok.jeeokmember.core.auth.controller.response.RefreshTokenResponse;
import com.jeeok.jeeokmember.core.auth.controller.response.SignupMemberResponse;
import com.jeeok.jeeokmember.core.auth.dto.JwtTokenDto;
import com.jeeok.jeeokmember.core.auth.service.AccessTokenService;
import com.jeeok.jeeokmember.core.auth.service.AuthService;
import com.jeeok.jeeokmember.core.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;
    private final CookieProvider cookieProvider;
    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<JsonResult> signup(@RequestBody @Validated SignupMemberRequest request) {

        //validate
        request.validate();

        Member signupMember = authService.signup(request.toMember());

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonResult.OK(new SignupMemberResponse(signupMember.getId())));
    }

    /**
     * access token 재발행
     */
    @GetMapping("/reissue")
    public ResponseEntity<JsonResult> refreshToken(@RequestHeader(value = "X-AUTH-TOKEN") String accessToken,
                                                   @CookieValue(value = "refresh-token") String refreshToken) {

        JwtTokenDto jwtTokenDto = refreshTokenService.refreshJwtToken(accessToken, refreshToken);

        ResponseCookie responseCookie = cookieProvider.createRefreshTokenCookie(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(JsonResult.OK(new RefreshTokenResponse(jwtTokenDto)));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<JsonResult> logout(@RequestHeader("X-AUTH-TOKEN") String accessToken) {

        refreshTokenService.logoutToken(accessToken);

        ResponseCookie responseCookie = cookieProvider.removeRefreshTokenCookie();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(JsonResult.OK());
    }

    /**
     * 토큰 조회
     */
    @GetMapping("/me")
    public ResponseEntity<JsonResult> checkAccessToken(@RequestHeader(name = "Authorization") String authrization) {

        accessTokenService.checkAccessToken(authrization);

        return ResponseEntity.status(HttpStatus.OK)
                .body(JsonResult.OK());
    }
}
