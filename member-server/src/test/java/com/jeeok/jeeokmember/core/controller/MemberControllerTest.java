package com.jeeok.jeeokmember.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokmember.common.json.Code;
import com.jeeok.jeeokmember.config.security.SecurityConfig;
import com.jeeok.jeeokmember.core.controller.request.SaveMemberRequest;
import com.jeeok.jeeokmember.core.controller.request.UpdateMemberRequest;
import com.jeeok.jeeokmember.core.domain.AuthType;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.domain.RoleType;
import com.jeeok.jeeokmember.core.dto.UpdateMemberParam;
import com.jeeok.jeeokmember.core.service.MemberService;
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
    public static final AuthType AUTH_TYPE = AuthType.JEEOK;
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1111", "2222");

    //UPDATE_MEMBER
    public static final String UPDATE_NAME = "update_jeeok";
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "3333", "44444");

    //ERROR_MESSAGE
    public static final String MEMBER = "Member";
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    //REQUEST_URL
    public static final String API_FIND_MEMBERS = "/api/members";
    public static final String API_FIND_MEMBER = "/api/members/{memberId}";
    public static final String API_SAVE_MEMBER = "/api/members";
    public static final String API_UPDATE_MEMBE = "/api/members/{memberId}";
    public static final String API_DELETE_MEMBER = "/api/members/{memberId}";

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
    @DisplayName("회원 목록 조회")
    void findMembers() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("회원 단건 조회")
    void findMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER);
        given(memberService.findMember(any(Long.class))).willReturn(member);

        //expected
        mockMvc.perform(get(API_FIND_MEMBER, 0L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.memberName").value(NAME))
                .andExpect(jsonPath("$.data.role").value(ROLE_TYPE.name()))
                .andExpect(jsonPath("$.data.phoneNumber").value(PHONE_NUMBER.fullPhoneNumber()))
                .andDo(print())
                .andDo(document("findMember",
                        /*requestHeaders(
                                headerWithName("memberId").description("회원 단건 조회")
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

        //verify
        verify(memberService, times(1)).findMember(any(Long.class));
    }

    @Test
    @DisplayName("회원 저장")
    void saveMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER);
        given(memberService.saveMember(any(Member.class))).willReturn(member);

        SaveMemberRequest request = SaveMemberRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .memberName(NAME)
                .role(ROLE_TYPE)
                .phoneNumber(PHONE_NUMBER.fullPhoneNumber())
                .build();

        //expected
        mockMvc.perform(post(API_SAVE_MEMBER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.savedMemberId").hasJsonPath())
                .andDo(print())
                .andDo(document("saveMember",
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.savedMemberId").description("저장된 회원 고유번호")
                        )
                ));

        //verify
        verify(memberService, times(1)).saveMember(any(Member.class));
    }

    @Test
    @DisplayName("회원 수정")
    void updateMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER);
        given(memberService.findMember(any(Long.class))).willReturn(member);

        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .memberName(UPDATE_NAME)
                .phoneNumber(UPDATE_PHONE_NUMBER.fullPhoneNumber())
                .build();

        //when
        mockMvc.perform(put(API_UPDATE_MEMBE, 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedMemberId").hasJsonPath())
                .andDo(print())
                .andDo(document("updateMember",
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.updatedMemberId").description("수정된 회원 고유번호")
                        )
                ));

        //verify
        verify(memberService, times(1)).updateMember(any(Long.class), any(UpdateMemberParam.class));
        verify(memberService, times(1)).findMember(any(Long.class));
    }

    @Test
    @DisplayName("회원 삭제")
    void deleteMember() throws Exception {
        //given

        //when
        mockMvc.perform(delete(API_DELETE_MEMBER, 0L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("deleteMember",
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data").description("데이터")
                        )
                ));

        //verify
        verify(memberService, times(1)).deleteMember(any(Long.class));
    }
}