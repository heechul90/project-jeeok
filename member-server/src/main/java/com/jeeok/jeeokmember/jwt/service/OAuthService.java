package com.jeeok.jeeokmember.jwt.service;

import com.jeeok.jeeokmember.common.utils.CookieProvider;
import com.jeeok.jeeokmember.common.utils.JwtTokenProvider;
import com.jeeok.jeeokmember.config.auth.PrincipalDetailService;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.repository.MemberRepository;
import com.jeeok.jeeokmember.jwt.dto.OAuthAttributeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final PrincipalDetailService principalDetailService;
    private final CookieProvider cookieProvider;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //OAuth 서비스 id
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //OAuth 로그인 진행시 키가 되는 필드값
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        //OAuth2UserService
        OAuthAttributeDto attributeDto = OAuthAttributeDto.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member savedMember = saveMember(attributeDto);

        String email = savedMember.getEmail();

        Collection<? extends GrantedAuthority> authorities = principalDetailService.loadUserByUsername(email).getAuthorities();

        return new DefaultOAuth2User(
                authorities,
                attributeDto.getAttributes(),
                attributeDto.getNameAttributeKey()
        );
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String email = String.valueOf(((DefaultOAuth2User) authentication.getPrincipal()).getAttributes().get("email"));

        String jwtRefreshToken = jwtTokenProvider.createJwtRefreshToken();
        Long memberId = memberRepository.findByEmail(email).get().getId();
        refreshTokenService.updateRefreshToken(memberId, jwtTokenProvider.getRefreshTokenId(jwtRefreshToken));

        //쿠키 설정
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(jwtRefreshToken);

        Cookie cookie = cookieProvider.of(refreshTokenCookie);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addCookie(cookie);

        //body 설정
        String jwtAccessToken = jwtTokenProvider.createJwtAccessToken(memberId.toString(), request.getRequestURI(), authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        Date expiredTime = jwtTokenProvider.getExpiredTime(jwtAccessToken);

        response.sendRedirect("http://localhost:5713/auth?" +
                "accessToken=" + jwtAccessToken +
                "&expiredTime=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime));
    }

    @Transactional
    public Member saveMember(OAuthAttributeDto attributeDto) {
        return memberRepository.save(
                memberRepository.findByEmail(attributeDto.getEmail())
                        .orElse(attributeDto.toMember(attributeDto))
        );
    }
}
