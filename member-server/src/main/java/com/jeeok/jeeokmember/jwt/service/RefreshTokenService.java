package com.jeeok.jeeokmember.jwt.service;

import com.jeeok.jeeokmember.common.exception.EntityNotFound;
import com.jeeok.jeeokmember.common.utils.JwtTokenProvider;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.repository.MemberRepository;
import com.jeeok.jeeokmember.jwt.dto.JwtTokenDto;
import com.jeeok.jeeokmember.jwt.exception.AccessTokenNotValidException;
import com.jeeok.jeeokmember.jwt.exception.RefreshTokenNotValidException;
import com.jeeok.jeeokmember.jwt.redis.RefreshToken;
import com.jeeok.jeeokmember.jwt.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    public void updateRefreshToken(Long memberId, String uuid) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFound("Member", memberId.toString()));

        refreshTokenRedisRepository.save(RefreshToken.of(findMember.getId().toString(), uuid));
    }

    @Transactional
    public JwtTokenDto refreshJwtToken(String accessToken, String refreshToken) {
        String memberId = jwtTokenProvider.getUserId(accessToken);
        RefreshToken findRefreshToken = refreshTokenRedisRepository.findById(memberId)
                .orElseThrow(() -> new RefreshTokenNotValidException("사용자 고유번호 : " + memberId + "는 등록된 리프레쉬 토큰이 없습니다."));

        //refresh token 검증
        String findRefreshTokenId = findRefreshToken.getRefreshTokenId();
        if (!jwtTokenProvider.validateJwtToken(findRefreshTokenId)) {
            refreshTokenRedisRepository.delete(findRefreshToken);
            throw new RefreshTokenNotValidException("Not validate jwt token, toekn = " + refreshToken);
        }

        if (!jwtTokenProvider.equalRefreshTokenId(findRefreshTokenId, refreshToken)) {
            throw new RefreshTokenNotValidException("redis 의 값과 일치하지 않습니다. refreshToken = " + refreshToken);
        }

        Member findMember = memberRepository.findById(Long.valueOf(memberId))
                .orElseThrow(() -> new EntityNotFound("Member", memberId));

        //access token
        Authentication authentication = getAuthentication(findMember.getEmail());
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String newAccessToken = jwtTokenProvider.createJwtAccessToken(memberId, "/reissu", roles);
        Date expiredTime = jwtTokenProvider.getExpiredTime(newAccessToken);

        return JwtTokenDto.builder()
                .accessToken(newAccessToken)
                .accessToken(refreshToken)
                .accessTokenExpiredDate(expiredTime)
                .build();
    }

    public void logoutToken(String accessToken) {
        if (!jwtTokenProvider.validateJwtToken(accessToken)) {
            throw new AccessTokenNotValidException("access token is not valid");
        }

        RefreshToken refreshToken = refreshTokenRedisRepository.findById(jwtTokenProvider.getUserId(accessToken))
                .orElseThrow(() -> new RefreshTokenNotValidException("refresh token is not exist"));

        refreshTokenRedisRepository.delete(refreshToken);
    }

    public Authentication getAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
