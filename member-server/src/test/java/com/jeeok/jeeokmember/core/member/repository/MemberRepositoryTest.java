package com.jeeok.jeeokmember.core.member.repository;

import com.jeeok.jeeokmember.core.RepositoryTest;
import com.jeeok.jeeokmember.core.member.domain.*;
import com.jeeok.jeeokmember.core.member.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.member.dto.SearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTest {

    //CREATE MEMBER
    public static final String EMAIL = "jeeok@gmail.com";
    public static final String PASSWORD = "1234";
    public static final String NAME = "jeeok";
    public static final RoleType ROLE_TYPE = RoleType.ROLE_USER;
    public static final AuthType AUTH_TYPE = AuthType.JEEOK;
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1111", "2222");
    public static final Address ADDRESS = new Address("83726", "서울시");

    @PersistenceContext protected EntityManager em;

    @Autowired protected MemberQueryRepository memberQueryRepository;

    @Autowired protected MemberRepository memberRepository;

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
        @DisplayName("멤버 목록 조회")
        void findMembers() {
            //given
            IntStream.range(0, 20).forEach(i -> em.persist(getMember(EMAIL + i, PASSWORD, NAME + i, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER, ADDRESS)));

            MemberSearchCondition condition = new MemberSearchCondition();
            condition.setSearchCondition(SearchCondition.NAME);
            condition.setSearchKeyword(NAME);

            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Member> content = memberQueryRepository.findMembers(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(20);
            assertThat(content.getContent().size()).isEqualTo(pageRequest.getPageSize());
        }
    }
}