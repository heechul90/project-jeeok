package com.jeeok.jeeokmember.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeeok.jeeokmember.common.dto.LoginRequest;
import com.jeeok.jeeokmember.common.json.JsonResult;
import com.jeeok.jeeokmember.common.utils.CookieProvider;
import com.jeeok.jeeokmember.common.utils.JwtTokenProvider;
import com.jeeok.jeeokmember.jwt.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CookieProvider cookieProvider;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        Authentication authentication;

        try {
            LoginRequest credential = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credential.getEmail(), credential.getPassword())
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String userId = user.getUsername();

        String jwtAccessToken = jwtTokenProvider.createJwtAccessToken(userId, request.getRequestURI(), roles);
        Date expiredTime = jwtTokenProvider.getExpiredTime(jwtAccessToken);
        String jwtRefreshToken = jwtTokenProvider.createJwtRefreshToken();

        refreshTokenService.updateRefreshToken(Long.valueOf(userId), jwtTokenProvider.getRefreshTokenId(jwtRefreshToken));

        //쿠키 설정
        ResponseCookie responseCookie = cookieProvider.createRefreshTokenCookie(jwtRefreshToken);

        Cookie cookie = cookieProvider.of(responseCookie);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addCookie(cookie);

        //body 설정
        Map<String, Object> tokens = Map.of(
                "accessToken", jwtAccessToken,
                "expiredTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime)
        );

        //java 8 localDateTime 직렬화 역직렬화 오류 해결 방법
        new ObjectMapper().registerModule(new JavaTimeModule()).writeValue(response.getOutputStream(), JsonResult.OK(tokens));
    }
}
