package com.jeeok.jeeokmember.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokmember.common.json.Code;
import com.jeeok.jeeokmember.common.utils.CookieProvider;
import com.jeeok.jeeokmember.config.security.SecurityConfig;
import com.jeeok.jeeokmember.core.MemberTestConfig;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.domain.RoleType;
import com.jeeok.jeeokmember.core.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = MemberController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriHost = "127.0.0.1", uriPort = 8001)
class MemberControllerTest {

    //CREATE MEMBER
    public static final String EMAIL = "jeeok@gmail.com";
    public static final String PASSWORD = "1234";
    public static final String NAME = "jeeok";
    public static final RoleType ROLE_TYPE = RoleType.ROLE_USER;
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1111", "2222");

    //UPDATE_MEMBER
    public static final String UPDATE_NAME = "update_jeeok";
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "3333", "44444");

    //ERROR_MESSAGE
    public static final String MEMBER = "Member";
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    @MockBean MemberService memberService;

    @Autowired ObjectMapper objectMapper;

    @Autowired MockMvc mockMvc;

    private Member getMember(String email, String password, String name, RoleType roleType, PhoneNumber phoneNumber) {
        return Member.createMember()
                .email(email)
                .password(password)
                .name(name)
                .roleType(roleType)
                .phoneNumber(phoneNumber)
                .build();
    }

    @Test
    void findMembers() {
        //given

        //when

        //then
    }

    @Test
    void findMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, PHONE_NUMBER);
        given(memberService.findMember(any(Long.class))).willReturn(member);

        //expected
        mockMvc.perform(get("/api/members/{memberId}", 0L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.memberName").value(NAME))
                .andExpect(jsonPath("$.data.role").value(ROLE_TYPE.name()))
                .andExpect(jsonPath("$.data.phoneNumber").value(PHONE_NUMBER.fullPhoneNumber()))
                .andDo(print())
                .andDo(document("find-member",
                        /*requestHeaders(
                                headerWithName("member-id").description("회원 단건 조회")
                        ),*/
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.memberId").description("회원 고유번호"),
                                fieldWithPath("data.email").description("회원 이메일"),
                                fieldWithPath("data.memberName").description("회원 이름"),
                                fieldWithPath("data.role").description("회원 권한"),
                                fieldWithPath("data.phoneNumber").description("회원 휴대폰번호")
                        )
                ));
        //then
    }

    @Test
    void saveMember() {
        //given

        //when

        //then
    }

    @Test
    void updateMember() {
        //given

        //when

        //then
    }

    @Test
    void deleteMember() {
        //given

        //when

        //then
    }
}