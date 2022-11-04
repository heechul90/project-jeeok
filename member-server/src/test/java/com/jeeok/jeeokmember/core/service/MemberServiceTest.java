package com.jeeok.jeeokmember.core.service;

import com.jeeok.jeeokmember.common.exception.EntityNotFound;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.domain.RoleType;
import com.jeeok.jeeokmember.core.dto.UpdateMemberParam;
import com.jeeok.jeeokmember.core.repository.MemberQueryRepository;
import com.jeeok.jeeokmember.core.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    //CREATE MEMBER
    public static final String EMAIL = "jeeok@gmail.com";
    public static final String PASSWORD = "1234";
    public static final String NAME = "jeeok";
    public static final RoleType ROLE_TYPE = RoleType.ROLE_USER;
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1111", "2222");
    public static final String UPDATE_NAME = "update_jeeok";
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "3333", "44444");
    @InjectMocks MemberService memberService;

    @Mock MemberQueryRepository memberQueryRepository;

    @Mock MemberRepository memberRepository;

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
    @DisplayName("멤버 목록 조회")
    void findMembers() {
        getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, PHONE_NUMBER);
    }

    @Test
    @DisplayName("멤버 단건 조회")
    void findMember() {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, PHONE_NUMBER);
        given(memberRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(member));

        //when
        Member findMember = memberService.findMember(0L);

        //then
        assertThat(findMember.getEmail()).isEqualTo(EMAIL);
        assertThat(findMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(findMember.getName()).isEqualTo(NAME);
        assertThat(findMember.getRoleType()).isEqualTo(ROLE_TYPE);
        assertThat(findMember.getPhoneNumber()).isEqualTo(PHONE_NUMBER);

        //verify
        verify(memberRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("멤버 단건 조회_예외")
    void findMember_entityNotFound() {
        //given


        //expected
    }

    @Test
    @DisplayName("멤버 저장")
    void saveMember() {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, PHONE_NUMBER);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        //when
        Member savedMember = memberService.saveMember(member);

        //then
        assertThat(savedMember.getEmail()).isEqualTo(EMAIL);
        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(savedMember.getName()).isEqualTo(NAME);
        assertThat(savedMember.getRoleType()).isEqualTo(ROLE_TYPE);
        assertThat(savedMember.getPhoneNumber()).isEqualTo(PHONE_NUMBER);

        //verify
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("멤버 수정")
    void updateMember() {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, PHONE_NUMBER);
        given(memberRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(member));

        UpdateMemberParam param = UpdateMemberParam.builder()
                .memberName(UPDATE_NAME)
                .phoneNumber(UPDATE_PHONE_NUMBER)
                .build();

        //when
        memberService.updateMember(0L, param);
        
        //then
        assertThat(member.getName()).isEqualTo(UPDATE_NAME);
        assertThat(member.getPhoneNumber().getFirst()).isEqualTo(UPDATE_PHONE_NUMBER.getFirst());
        assertThat(member.getPhoneNumber().getMiddle()).isEqualTo(UPDATE_PHONE_NUMBER.getMiddle());
        assertThat(member.getPhoneNumber().getLast()).isEqualTo(UPDATE_PHONE_NUMBER.getLast());

        //verify
        verify(memberRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("멤버 삭제")
    void deleteMember() {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, PHONE_NUMBER);
        given(memberRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(member));

        //when
        memberService.deleteMember(0L);

        //then

        //verify
        verify(memberRepository, times(1)).findById(any(Long.class));
        verify(memberRepository, times(1)).delete(any(Member.class));
    }
}