package com.jeeok.jeeokmember;

import com.jeeok.jeeokmember.common.entity.Address;
import com.jeeok.jeeokmember.common.entity.PhoneNumber;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final InitService initService;

    @PostConstruct
    public void init() {

        //멤버
        initService.memberInit();

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        @PersistenceContext public final EntityManager em;

        private static Member getMember(String email, String password, String name, RoleType roleType, AuthType authType, PhoneNumber phoneNumber, Address address) {
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

        public void memberInit() {
            Member spring1 = getMember(
                    "spring1",
                    "1234",
                    "스프링1",
                    RoleType.ROLE_USER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "2397", "6591"),
                    new Address("83671", "서울시")
            );
            Member spring2 = getMember(
                    "spring2",
                    "1234",
                    "스프링2",
                    RoleType.ROLE_USER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "4422", "6242"),
                    new Address("87250", "성남시")
            );
            Member managerMember = getMember(
                    "manager1",
                    "1234",
                    "교촌치킨 매니저",
                    RoleType.ROLE_MANAGER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "5678", "2345"),
                    new Address("55237", "논산시")
            );
            Member manager2 = getMember(
                    "manager2",
                    "1234",
                    "BHC치킨 매니저",
                    RoleType.ROLE_MANAGER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "3432", "2356"),
                    new Address("43255", "대전시")
            );
            Member rider1 = getMember(
                    "deliveryman1",
                    "1234",
                    "배달원1",
                    RoleType.ROLE_RIDER,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "3411", "9266"),
                    new Address("34328", "천안시")
            );
            Member admin = getMember(
                    "admin",
                    "1234",
                    "관리자",
                    RoleType.ROLE_ADMIN,
                    AuthType.JEEOK,
                    new PhoneNumber("010", "1111", "2222"),
                    new Address("22313", "세종시")
            );
            em.persist(spring1);
            em.persist(spring2);
            em.persist(managerMember);
            em.persist(manager2);
            em.persist(rider1);
            em.persist(admin);
        }
    }
}
