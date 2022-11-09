package com.jeeok.jeeokmember.core.service;

import com.jeeok.jeeokmember.common.exception.EntityNotFound;
import com.jeeok.jeeokmember.core.domain.AuthType;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.domain.RoleType;
import com.jeeok.jeeokmember.core.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.dto.SaveMemberParam;
import com.jeeok.jeeokmember.core.dto.UpdateMemberParam;
import com.jeeok.jeeokmember.core.repository.MemberQueryRepository;
import com.jeeok.jeeokmember.core.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

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
        //given
        List<Member> members = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> members.add(getMember(EMAIL + i, PASSWORD, NAME, ROLE_TYPE, PHONE_NUMBER)));
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
    @DisplayName("멤버 저장")
    void saveMember() {
        //given
        Member member = getMember(EMAIL, PASSWORD, NAME, ROLE_TYPE, PHONE_NUMBER);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        SaveMemberParam param = SaveMemberParam.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .name(NAME)
                .roleType(ROLE_TYPE)
                .authType(AUTH_TYPE)
                .phoneNumber(PHONE_NUMBER)
                .build();

        //when
        Member savedMember = memberService.saveMember(param);

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
                .name(UPDATE_NAME)
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

    @Test
    @DisplayName("멤버 findById_예외")
    void findMember_entityNotFound() {
        //given
        given(memberRepository.findById(any(Long.class))).willThrow(new EntityNotFound(MEMBER, NOT_FOUND_ID.toString()));

        //expected
        assertThatThrownBy(() -> memberService.findMember(NOT_FOUND_ID))
                .isInstanceOf(EntityNotFound.class)
                .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + MEMBER)
                .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);

        assertThatThrownBy(() -> memberService.updateMember(NOT_FOUND_ID, any(UpdateMemberParam.class)))
                .isInstanceOf(EntityNotFound.class)
                .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + MEMBER)
                .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);

        assertThatThrownBy(() -> memberService.deleteMember(NOT_FOUND_ID))
                .isInstanceOf(EntityNotFound.class)
                .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + MEMBER)
                .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ID);

        //verify
        verify(memberRepository, times(3)).findById(any(Long.class));
    }
}