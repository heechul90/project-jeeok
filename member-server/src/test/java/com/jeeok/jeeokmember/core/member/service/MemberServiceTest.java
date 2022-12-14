package com.jeeok.jeeokmember.core.member.service;

import com.jeeok.jeeokmember.common.entity.Address;
import com.jeeok.jeeokmember.common.entity.PhoneNumber;
import com.jeeok.jeeokmember.core.MockTest;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import com.jeeok.jeeokmember.core.member.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.member.dto.SaveMemberParam;
import com.jeeok.jeeokmember.core.member.dto.UpdateMemberParam;
import com.jeeok.jeeokmember.core.member.exception.MemberNotFound;
import com.jeeok.jeeokmember.core.member.repository.MemberQueryRepository;
import com.jeeok.jeeokmember.core.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MemberServiceTest extends MockTest {

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
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "3333", "44444");
    public static final Address UPDATE_ADDRESS = new Address("99802", "?????????");

    //ERROR_MESSAGE
    public static final Long NOT_FOUND_ID = 1L;
    public static final String HAS_MESSAGE = "???????????? ?????? ???????????????. ?????? ????????????=" + NOT_FOUND_ID;

    @InjectMocks protected MemberService memberService;
    @Mock protected MemberQueryRepository memberQueryRepository;
    @Mock protected MemberRepository memberRepository;

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

    @Nested
    class SuccessfulTest {
        @Test
        @DisplayName("?????? ?????? ??????")
        void findMembers() {
            //given
            List<Member> members = new ArrayList<>();
            IntStream.range(0, 5).forEach(i -> members.add(getMember(EMAIL + i, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS)));
            given(memberQueryRepository.findMembers(any(MemberSearchCondition.class), any(Pageable.class))).willReturn(new PageImpl<>(members));

            MemberSearchCondition confition = new MemberSearchCondition();
            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Member> content = memberService.findMembers(confition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(5);
            assertThat(content.getContent().size()).isEqualTo(5);
        }

        @Test
        @DisplayName("?????? ?????? ??????")
        void findMember() {
            //given
            Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
            given(memberRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(member));

            //when
            Member findMember = memberService.findMember(0L);

            //then
            assertThat(findMember.getEmail()).isEqualTo(EMAIL);
            assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
            assertThat(findMember.getName()).isEqualTo(NAME);
            assertThat(findMember.getRoleType()).isEqualTo(ROLE_TYPE);
            assertThat(findMember.getAuthType()).isEqualTo(AUTH_TYPE);
            assertThat(findMember.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
            assertThat(findMember.getAddress()).isEqualTo(ADDRESS);

            //verify
            verify(memberRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("?????? ??????")
        void saveMember() {
            //given
            Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
            given(memberRepository.save(any(Member.class))).willReturn(member);

            SaveMemberParam param = SaveMemberParam.builder()
                    .email(EMAIL)
                    .password(PASSWORD)
                    .name(NAME)
                    .roleType(ROLE_TYPE)
                    .authType(AUTH_TYPE)
                    .phoneNumber(PHONE_NUMBER)
                    .address(ADDRESS)
                    .build();

            //when
            Member savedMember = memberService.saveMember(param);

            //then
            assertThat(savedMember.getEmail()).isEqualTo(EMAIL);
            assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
            assertThat(savedMember.getName()).isEqualTo(NAME);
            assertThat(savedMember.getRoleType()).isEqualTo(ROLE_TYPE);
            assertThat(savedMember.getAuthType()).isEqualTo(AUTH_TYPE);
            assertThat(savedMember.getPhoneNumber()).isEqualTo(PHONE_NUMBER);
            assertThat(savedMember.getAddress()).isEqualTo(ADDRESS);

            //verify
            verify(memberRepository, times(1)).save(any(Member.class));
        }

        @Test
        @DisplayName("?????? ??????")
        void updateMember() {
            //given
            Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
            given(memberRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(member));

            UpdateMemberParam param = UpdateMemberParam.builder()
                    .name(UPDATE_NAME)
                    .phoneNumber(UPDATE_PHONE_NUMBER)
                    .address(UPDATE_ADDRESS)
                    .build();

            //when
            memberService.updateMember(0L, param);

            //then
            assertThat(member.getName()).isEqualTo(UPDATE_NAME);
            assertThat(member.getPhoneNumber().getFirst()).isEqualTo(UPDATE_PHONE_NUMBER.getFirst());
            assertThat(member.getPhoneNumber().getMiddle()).isEqualTo(UPDATE_PHONE_NUMBER.getMiddle());
            assertThat(member.getPhoneNumber().getLast()).isEqualTo(UPDATE_PHONE_NUMBER.getLast());
            assertThat(member.getAddress().getZipcode()).isEqualTo(UPDATE_ADDRESS.getZipcode());
            assertThat(member.getAddress().getAddress()).isEqualTo(UPDATE_ADDRESS.getAddress());

            //verify
            verify(memberRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("?????? ??????")
        void deleteMember() {
            //given
            Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS);
            given(memberRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(member));

            //when
            memberService.deleteMember(0L);

            //then

            //verify
            verify(memberRepository, times(1)).findById(any(Long.class));
            verify(memberRepository, times(1)).delete(any(Member.class));
        }
    }

    @Nested
    class EntityNotFoundTest {
        @Test
        @DisplayName("?????? ?????? ??????_??????")
        void findMember_exception() {
            //given
            given(memberRepository.findById(any(Long.class))).willThrow(new MemberNotFound(NOT_FOUND_ID));

            //expected
            assertThatThrownBy(() -> memberService.findMember(NOT_FOUND_ID))
                    .isInstanceOf(MemberNotFound.class)
                    .hasMessage(HAS_MESSAGE);

            //then
            verify(memberRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("?????? ??????_??????")
        void updateMember_exception() {
            //given
            given(memberRepository.findById(any(Long.class))).willThrow(new MemberNotFound(NOT_FOUND_ID));

            //expected
            assertThatThrownBy(() -> memberService.updateMember(NOT_FOUND_ID, any(UpdateMemberParam.class)))
                    .isInstanceOf(MemberNotFound.class)
                    .hasMessage(HAS_MESSAGE);

            //then
            verify(memberRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("?????? ??????_??????")
        void deleteMember_exception() {
            //given
            given(memberRepository.findById(any(Long.class))).willThrow(new MemberNotFound(NOT_FOUND_ID));

            //expected
            assertThatThrownBy(() -> memberService.deleteMember(NOT_FOUND_ID))
                    .isInstanceOf(MemberNotFound.class)
                    .hasMessage(HAS_MESSAGE);

            //then
            verify(memberRepository, times(1)).findById(any(Long.class));
        }
    }
}