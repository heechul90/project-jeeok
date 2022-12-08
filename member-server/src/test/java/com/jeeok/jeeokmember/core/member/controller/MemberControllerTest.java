package com.jeeok.jeeokmember.core.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokmember.common.entity.PhoneNumber;
import com.jeeok.jeeokmember.common.json.Code;
import com.jeeok.jeeokmember.core.IntegrationTest;
import com.jeeok.jeeokmember.core.member.controller.request.MemberEditRequest;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends IntegrationTest {

    //CREATE_MEMBER
    public static final Long MEMBER_ID_1 = 1L;
    public static final String EMAIL = "gildong-hong@jeeok.com";
    public static final String PASSWORD = "1234";
    public static final String NAME = "홍길동";
    public static final RoleType ROLE_TYPE = RoleType.ROLE_USER;
    public static final AuthType AUTH_TYPE = AuthType.JEEOK;
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1234", "5678");

    //EDIT_REQUEST
    public static final String EDIT_NAME = "홍길순";
    public static final PhoneNumber EDIT_PHONE_NUMBER = new PhoneNumber("010", "8765", "4321");

    //REQUEST_INFO
    public static final String HEADER_NAME = "member-id";
    public static final String API_MEMBER_INFO = "/member/info";
    public static final String API_MEMBER_EDIT = "/member/{memberId}/edit";

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @PersistenceContext protected EntityManager em;
    @Autowired protected MemberService memberService;

    private Member getMember(String email, String password, String name, RoleType roleType, AuthType authType, PhoneNumber phoneNumber) {
        return Member.createMember()
                .email(email)
                .password(password)
                .name(name)
                .roleType(roleType)
                .authType(authType)
                .phoneNumber(phoneNumber)
                .build();
    }

    @Test
    @DisplayName("내 정보 조회")
    void info() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER);
        em.persist(member);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_MEMBER_INFO)
                .header(HEADER_NAME, member.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_time").isNotEmpty())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.memberName").value(NAME))
                .andExpect(jsonPath("$.data.phoneNumber").value(PHONE_NUMBER.fullPhoneNumber()))
                .andDo(print())
                .andDo(document("memberInfo",
                        requestHeaders(
                                headerWithName("member-id").description("회원 고유번호(입력하지 말것!)")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.memberId").description("회원 고유번호"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.memberName").description("회원 이름"),
                                fieldWithPath("data.phoneNumber").description("회원 휴대폰번호")
                        )
                ));
    }

    @Test
    @DisplayName("내 정보 수정")
    void edit() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER);
        em.persist(member);

        MemberEditRequest request = MemberEditRequest.builder()
                .memberName(EDIT_NAME)
                .phoneNumber(EDIT_PHONE_NUMBER.fullPhoneNumber())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_MEMBER_EDIT, member.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("memberEdit",
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("memberName").description("회원 이름"),
                                fieldWithPath("phoneNumber").description("회원 휴대폰번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.memberId").description("회원 고유번호")
                        )
                ));
    }
}