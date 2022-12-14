package com.jeeok.jeeokmember.core.member.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokmember.common.entity.Address;
import com.jeeok.jeeokmember.common.entity.PhoneNumber;
import com.jeeok.jeeokmember.common.json.Code;
import com.jeeok.jeeokmember.core.IntegrationTest;
import com.jeeok.jeeokmember.core.member.controller.request.SaveMemberRequest;
import com.jeeok.jeeokmember.core.member.controller.request.UpdateMemberRequest;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import com.jeeok.jeeokmember.core.member.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.member.dto.SearchCondition;
import com.jeeok.jeeokmember.core.member.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMemberControllerTest extends IntegrationTest {

    //CREATE MEMBER
    public static final String EMAIL = "jeeok@gmail.com";
    public static final String PASSWORD = "1234";
    public static final String NAME = "jeeok";
    public static final RoleType ROLE_TYPE = RoleType.ROLE_USER;
    public static final AuthType AUTH_TYPE = AuthType.JEEOK;
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1111", "2222");
    public static final Address ADDRESS = new Address("83726", "?????????");

    //UPDATE_MEMBER
    public static final String UPDATE_NAME = "update_jeeok";
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "3333", "1234");
    public static final Address UPDATE_ADDRESS = new Address("99802", "?????????");

    //ERROR_MESSAGE

    //REQUEST_INFO
    public static final String API_FIND_MEMBERS = "/admin/members";
    public static final String API_FIND_MEMBER = "/admin/members/{memberId}";
    public static final String API_SAVE_MEMBER = "/admin/members";
    public static final String API_UPDATE_MEMBER = "/admin/members/{memberId}";
    public static final String API_DELETE_MEMBER = "/admin/members/{memberId}";

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @PersistenceContext protected EntityManager em;
    @Autowired protected MemberService memberService;

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
    @DisplayName("?????? ?????? ??????")
    void findMembers() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getMember(EMAIL + i, PASSWORD, NAME + i, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS)));

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
        ResultActions resultActions = mockMvc.perform(get(API_FIND_MEMBERS)
                .queryParams(conditionParams)
                .queryParams(pageRequestParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(10)))
                .andDo(print())
                .andDo(document("admin-findMembers",
                        requestParameters(
                                parameterWithName("searchCondition").description("?????? ??????"),
                                parameterWithName("searchKeyword").description("?????? ?????????"),
                                parameterWithName("page").description("?????? ?????????"),
                                parameterWithName("size").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data[*].memberId").description("?????? ????????????"),
                                fieldWithPath("data[*].email").description("?????????"),
                                fieldWithPath("data[*].memberName").description("?????? ??????"),
                                fieldWithPath("data[*].role").description("?????? ??????"),
                                fieldWithPath("data[*].auth").description("?????? ????????? ??????"),
                                fieldWithPath("data[*].phoneNumber").description("?????? ???????????????"),
                                fieldWithPath("data[*].address.zipcode").description("????????????"),
                                fieldWithPath("data[*].address.address").description("?????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void findMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
        em.persist(member);

        //expected
        ResultActions resultActions = mockMvc.perform(get(API_FIND_MEMBER, member.getId()));

        //then
        resultActions
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
                .andDo(document("admin-findMember",
                        pathParameters(
                                parameterWithName("memberId").description("?????? ?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.memberId").description("?????? ????????????"),
                                fieldWithPath("data.email").description("?????????"),
                                fieldWithPath("data.memberName").description("?????? ??????"),
                                fieldWithPath("data.role").description("?????? ??????"),
                                fieldWithPath("data.auth").description("?????? ????????? ??????"),
                                fieldWithPath("data.phoneNumber").description("?????? ???????????????"),
                                fieldWithPath("data.address.zipcode").description("????????????"),
                                fieldWithPath("data.address.address").description("?????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void saveMember() throws Exception {
        //given
        SaveMemberRequest request = SaveMemberRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .memberName(NAME)
                .roleType(ROLE_TYPE)
                .phoneNumber(PHONE_NUMBER.fullPhoneNumber())
                .zipcode(ADDRESS.getZipcode())
                .address(ADDRESS.getAddress())
                .build();

        //expected
        ResultActions resultActions = mockMvc.perform(post(API_SAVE_MEMBER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.savedMemberId").hasJsonPath())
                .andDo(print())
                .andDo(document("admin-saveMember",
                        requestFields(
                                fieldWithPath("email").description("?????????"),
                                fieldWithPath("password").description("????????????"),
                                fieldWithPath("memberName").description("?????? ??????"),
                                fieldWithPath("roleType").description("?????? ?????? ??????"),
                                fieldWithPath("phoneNumber").description("?????? ???????????????"),
                                fieldWithPath("zipcode").description("????????????"),
                                fieldWithPath("address").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.savedMemberId").description("????????? ?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void updateMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
        em.persist(member);

        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .memberName(UPDATE_NAME)
                .phoneNumber(UPDATE_PHONE_NUMBER.fullPhoneNumber())
                .zipcode(UPDATE_ADDRESS.getZipcode())
                .address(UPDATE_ADDRESS.getAddress())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_UPDATE_MEMBER, member.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedMemberId").hasJsonPath())
                .andDo(print())
                .andDo(document("admin-updateMember",
                        pathParameters(
                                parameterWithName("memberId").description("?????? ????????????")
                        ),
                        requestFields(
                                fieldWithPath("memberName").description("?????? ??????"),
                                fieldWithPath("phoneNumber").description("?????? ???????????????"),
                                fieldWithPath("zipcode").description("????????????"),
                                fieldWithPath("address").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.updatedMemberId").description("????????? ?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void deleteMember() throws Exception {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
        em.persist(member);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_MEMBER, member.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-deleteMember",
                        pathParameters(
                                parameterWithName("memberId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").description("?????????")
                        )
                ));
    }
}