package com.jeeok.jeeokmember.core.member.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokmember.common.json.Code;
import com.jeeok.jeeokmember.config.security.SecurityConfig;
import com.jeeok.jeeokmember.core.member.controller.request.SaveMemberRequest;
import com.jeeok.jeeokmember.core.member.controller.request.UpdateMemberRequest;
import com.jeeok.jeeokmember.core.member.domain.*;
import com.jeeok.jeeokmember.core.member.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.member.dto.SaveMemberParam;
import com.jeeok.jeeokmember.core.member.dto.SearchCondition;
import com.jeeok.jeeokmember.core.member.dto.UpdateMemberParam;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AdminMemberController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)}
)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "member.jeeok.com", uriPort = 443)
class AdminMemberControllerTest {

    //CREATE MEMBER
    public static final String EMAIL = "jeeok@gmail.com";
    public static final String PASSWORD = "1234";
    public static final String NAME = "jeeok";
    public static final RoleType ROLE_TYPE = RoleType.ROLE_USER;
    public static final AuthType AUTH_TYPE = AuthType.JEEOK;
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1111", "2222");
    public static final Address ADDRESS = new Address("83726", "서울시");

    //UPDATE_MEMBER
    public static final String UPDATE_NAME = "update_jeeok";
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "3333", "44444");
    public static final Address UPDATE_ADDRESS = new Address("99802", "세종시");

    //ERROR_MESSAGE
    public static final String MEMBER = "Member";
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    //REQUEST_INFO
    public static final String API_FIND_MEMBERS = "/admin/members";
    public static final String API_FIND_MEMBER = "/admin/members/{memberId}";
    public static final String API_SAVE_MEMBER = "/admin/members";
    public static final String API_UPDATE_MEMBER = "/admin/members/{memberId}";
    public static final String API_DELETE_MEMBER = "/admin/members/{memberId}";

    @MockBean MemberService memberService;

    @Autowired ObjectMapper objectMapper;

    @Autowired MockMvc mockMvc;

    private Member getMember(String email, String password, String name, RoleType roleType, AuthType authType, PhoneNumber phoneNumber, Address address) {
        return Member.createMember()
                .email(email)
                .password(password)
                .name(name)
                .roleType(roleType)
                .authType(authType)
                .phoneNumber(phoneNumber)
                .address(address)
                .build();
    }

    @Test
    @DisplayName("회원 목록 조회")
    void findMembers() throws Exception {
        //given
        List<Member> members = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> members.add(getMember(EMAIL + i, PASSWORD, NAME + i, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS)));
        given(memberService.findMembers(any(MemberSearchCondition.class), any(Pageable.class))).willReturn(new PageImpl<>(members));

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setSearchCondition(SearchCondition.NAME);
        condition.setSearchKeyword(NAME);

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        mockMvc.perform(get(API_FIND_MEMBERS)
                        .queryParams(conditionParams)
                        .queryParams(pageRequestParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(5)))
                .andDo(print())
                .andDo(document("findMembers",
                        requestParameters(
                                parameterWithName("searchCondition").description("검색 조건"),
                                parameterWithName("searchKeyword").description("검색 키워드"),
                                parameterWithName("page").description("검색 페이지"),
                                parameterWithName("size").description("검색 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data[*].memberId").description("회원 고유번호"),
                                fieldWithPath("data[*].email").description("이메일"),
                                fieldWithPath("data[*].memberName").description("회원 이름"),
                                fieldWithPath("data[*].role").description("회원 권한"),
                                fieldWithPath("data[*].auth").description("회원 로그인 유형"),
                                fieldWithPath("data[*].phoneNumber").description("회원 휴대폰번호"),
                                fieldWithPath("data[*].address").description("회원 주소")
                        )
                ));

        //verify
        verify(memberService, times(1)).findMembers(any(MemberSearchCondition.class), any(Pageable.class));
    }

    @Test
    @DisplayName("회원 단건 조회")
    void findMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
        given(memberService.findMember(any(Long.class))).willReturn(member);

        //expected
        mockMvc.perform(get(API_FIND_MEMBER, 0L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.email").value(EMAIL))
                .andExpect(jsonPath("$.data.memberName").value(NAME))
                .andExpect(jsonPath("$.data.role").value(ROLE_TYPE.getTypeName()))
                .andExpect(jsonPath("$.data.auth").value(AUTH_TYPE.getTypeName()))
                .andExpect(jsonPath("$.data.phoneNumber").value(PHONE_NUMBER.fullPhoneNumber()))
                .andDo(print())
                .andDo(document("findMember",
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유 번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.memberId").description("회원 고유번호"),
                                fieldWithPath("data.email").description("이메일"),
                                fieldWithPath("data.memberName").description("회원 이름"),
                                fieldWithPath("data.role").description("회원 권한"),
                                fieldWithPath("data.auth").description("회원 로그인 유형"),
                                fieldWithPath("data.phoneNumber").description("회원 휴대폰번호"),
                                fieldWithPath("data.address").description("회원 주소")
                        )
                ));

        //verify
        verify(memberService, times(1)).findMember(any(Long.class));
    }

    @Test
    @DisplayName("회원 저장")
    void saveMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
        given(memberService.saveMember(any(SaveMemberParam.class))).willReturn(member);

        SaveMemberRequest request = SaveMemberRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .memberName(NAME)
                .role(ROLE_TYPE)
                .phoneNumber(PHONE_NUMBER.fullPhoneNumber())
                .zipcode(ADDRESS.getZipcode())
                .address(ADDRESS.getAddress())
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
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("memberName").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("role").type(ROLE_TYPE).description("회원 권한"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("회원 휴대폰번호"),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("우편번호"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.savedMemberId").description("저장된 회원 고유번호")
                        )
                ));

        //verify
        verify(memberService, times(1)).saveMember(any(SaveMemberParam.class));
    }

    @Test
    @DisplayName("회원 수정")
    void updateMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
        given(memberService.findMember(any(Long.class))).willReturn(member);

        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .memberName(UPDATE_NAME)
                .phoneNumber(UPDATE_PHONE_NUMBER.fullPhoneNumber())
                .zipcode(UPDATE_ADDRESS.getZipcode())
                .address(UPDATE_ADDRESS.getAddress())
                .build();

        //when
        mockMvc.perform(put(API_UPDATE_MEMBER, 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedMemberId").hasJsonPath())
                .andDo(print())
                .andDo(document("updateMember",
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("memberName").description("회원 이름"),
                                fieldWithPath("phoneNumber").description("회원 휴대폰번호"),
                                fieldWithPath("zipcode").type(JsonFieldType.STRING).description("우편번호"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("주소")
                        ),
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

        //expected
        mockMvc.perform(delete(API_DELETE_MEMBER, 0L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("deleteMember",
                        pathParameters(
                                parameterWithName("memberId").description("회원 고유번호")
                        ),
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