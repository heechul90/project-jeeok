package com.jeeok.jeeokmember.core.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokmember.common.utils.CookieProvider;
import com.jeeok.jeeokmember.config.security.SecurityConfig;
import com.jeeok.jeeokmember.core.MemberTestConfig;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@AutoConfigureMockMvc(addFilters = false)
@Import(MemberTestConfig.class)
@AutoConfigureRestDocs(uriHost = "127.0.0.1", uriPort = 12000)
class AuthControllerTest {

    @Autowired ObjectMapper objectMapper;

    @Autowired MockMvc mockMvc;

    @MockBean MemberService memberService;

    @SpyBean CookieProvider cookieProvider;

    @Test
    @DisplayName("회원 가입")
    void signup() {
        //given

        //when

        //then
    }

    @Test
    void refreshToken() {
    }

    @Test
    void logout() {
    }

    @Test
    void checkAccessToken() {
    }
}