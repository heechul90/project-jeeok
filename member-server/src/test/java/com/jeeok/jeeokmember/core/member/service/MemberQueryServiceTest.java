package com.jeeok.jeeokmember.core.member.service;

import com.jeeok.jeeokmember.core.MemberTestConfig;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import com.jeeok.jeeokmember.core.member.dto.MemberSearchCondition;
import com.jeeok.jeeokmember.core.member.dto.SearchCondition;
import com.jeeok.jeeokmember.core.member.repository.MemberQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(MemberTestConfig.class)
public class MemberQueryServiceTest {

    //CREATE MEMBER
    public static final String EMAIL = "jeeok@gmail.com";
    public static final String PASSWORD = "1234";
    public static final String NAME = "jeeok";
    public static final RoleType ROLE_TYPE = RoleType.ROLE_USER;
    public static final AuthType AUTH_TYPE = AuthType.JEEOK;
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1111", "2222");

    @PersistenceContext EntityManager em;

    @Autowired MemberService memberService;

    @Autowired MemberQueryRepository memberQueryRepository;

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
    @DisplayName("멤버 목록 조회")
    void findMembers() {
        //given
        IntStream.range(0, 20).forEach(i -> em.persist(getMember(EMAIL + i, PASSWORD, NAME + i, ROLE_TYPE, AUTH_TYPE, PHONE_NUMBER)));

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setSearchCondition(SearchCondition.NAME);
        condition.setSearchKeyword(NAME);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<Member> content = memberService.findMembers(condition, pageRequest);

        //then
        assertThat(content.getTotalElements()).isEqualTo(20);
        assertThat(content.getContent().size()).isEqualTo(pageRequest.getPageSize());
    }
}
