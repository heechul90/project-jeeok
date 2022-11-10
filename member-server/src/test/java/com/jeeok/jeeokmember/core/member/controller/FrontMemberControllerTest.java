package com.jeeok.jeeokmember.core.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokmember.common.json.Code;
import com.jeeok.jeeokmember.config.security.SecurityConfig;
import com.jeeok.jeeokmember.core.member.controller.request.MemberEditRequest;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import com.jeeok.jeeokmember.core.member.dto.UpdateMemberParam;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

@WebMvcTest(
        controllers = FrontMemberController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "member.jeeok.com", uriPort = 443)
class FrontMemberControllerTest {

    //CREATE_MEMBER
    public static final Long MEMBER_ID = 1L;
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
    public static final String API_MEMBER_INFO = "/front/member/info";
    public static final String API_MEMBER_EDIT = "/front/member/{memberId}/edit";

    @MockBean MemberService memberService;

    @Autowired ObjectMapper objectMapper;

    @Autowired MockMvc mockMvc;

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
        given(memberService.findMember(any(Long.class))).willReturn(member);

        //expected
        mockMvc.perform(get(API_MEMBER_INFO)
                        .header(HEADER_NAME, MEMBER_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_time").isNotEmpty())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.memberName").value(NAME))
                .andExpect(jsonPath("$.data.phoneNumber").value(PHONE_NUMBER.fullPhoneNumber()))
                .andDo(print())
                .andDo(document("member-info",
                        requestHeaders(
                                headerWithName("member-id").description("로그인한 멤버 고유번호")
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

        //verify
        verify(memberService, times(1)).findMember(any(Long.class));
    }

    @Test
    @DisplayName("내 정보 수정")
    void edit() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER);
        given(memberService.findMember(any(Long.class))).willReturn(member);

        MemberEditRequest request = MemberEditRequest.builder()
                .memberName(EDIT_NAME)
                .phoneNumber(EDIT_PHONE_NUMBER.fullPhoneNumber())
                .build();

        //expected
        mockMvc.perform(put(API_MEMBER_EDIT, MEMBER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("member-edit",
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
                ))
        ;

        //verify
        verify(memberService, times(1)).updateMember(any(Long.class), any(UpdateMemberParam.class));
        verify(memberService, times(1)).findMember(any(Long.class));
    }
}