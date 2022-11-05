package com.jeeok.jeeokmember.config.security;

import com.jeeok.jeeokmember.common.utils.CookieProvider;
import com.jeeok.jeeokmember.common.utils.JwtTokenProvider;
import com.jeeok.jeeokmember.jwt.service.OAuthService;
import com.jeeok.jeeokmember.jwt.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity // 시큐리티 활성화가 되어있는데 이와 관련된 설정을 이 파일에서 하겠다는 뜻 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final CookieProvider cookieProvider;
    private final UserDetailsService userDetailsService;
    private final OAuthService oAuthService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LoginAuthenticationFilter loginAuthenticationFilter =
                new LoginAuthenticationFilter(authenticationManagerBean(), jwtTokenProvider, refreshTokenService, cookieProvider);
        loginAuthenticationFilter.setFilterProcessesUrl("/login");

        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().anyRequest().permitAll();

        http.logout()
                .logoutUrl("/logout")
                .deleteCookies("refresh-token");

        http.oauth2Login()
                .userInfoEndpoint()
                .userService(oAuthService)
                .and()
                .failureUrl("http://localhost:5713/login")
                .successHandler(oAuthService::onAuthenticationSuccess);

        http.addFilter(loginAuthenticationFilter);
//        http.addFilterBefore(new HeaderAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    /*@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }*/

    /*@Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        LoginAuthenticationFilter loginAuthenticationFilter =
                new LoginAuthenticationFilter(authenticationManager(), jwtTokenProvider, refreshTokenService, cookieProvider);
        return http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().disable()

                //시큐리티는 기본적으로 세션을 사용하지만 여기서는 세션을 사용하지 않기 때문에 세션으로 Stateless 로 설정
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                //로그인, 회원가입 API는 토큰이 없는 상태에서 요철이 들어오기 때문에 permitAll 설정
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()

                .logout()
                .logoutUrl("/logout")
                .deleteCookies("refresh-token")
                .and()

                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuthService)
                .and()

                .failureUrl("http://localhost:12000/login")
                .successHandler(oAuthService::onAuthenticationSuccess)
                .and()

                .addFilter(loginAuthenticationFilter)
                .build();
    }*/


    /*@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
