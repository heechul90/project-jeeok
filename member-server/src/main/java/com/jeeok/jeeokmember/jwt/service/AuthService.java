package com.jeeok.jeeokmember.jwt.service;

import com.jeeok.jeeokmember.common.utils.JwtTokenProvider;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.repository.MemberRepository;
import com.jeeok.jeeokmember.jwt.dto.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입
     */
    @Transactional
    public Member signup(Member member) {
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 회원입니다.");
        }
        return memberRepository.save(member);
    }

    /**
     * 로그인
     */
    public JwtTokenDto login(String email, String password) {
        //email, password 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        //실제로 검증(email, password check)이 이루어지는 부분
        //authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("회원 없음"));

        List<String> roles = authenticationToken.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());


        String jwtAccessToken = jwtTokenProvider.createJwtAccessToken(String.valueOf(findMember.getId()), "temp", roles);
        Date expiredTime = jwtTokenProvider.getExpiredTime(jwtAccessToken);
        String jwtRefreshToken = jwtTokenProvider.createJwtRefreshToken();


        return JwtTokenDto.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .accessTokenExpiredDate(expiredTime)
                .build();
    }
}
