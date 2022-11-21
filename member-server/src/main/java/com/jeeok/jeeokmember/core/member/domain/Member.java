package com.jeeok.jeeokmember.core.member.domain;

import com.jeeok.jeeokmember.common.entity.BaseTimeEntity;
import com.jeeok.jeeokmember.core.member.dto.UpdateMemberParam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    private AuthType authType;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Address address;

    //===생성자 메서드===//
    /** Member 생성 */
    @Builder(builderMethodName = "createMember")
    public Member(String email, String password, String name, RoleType roleType, AuthType authType, PhoneNumber phoneNumber, Address address) {
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.name = name;
        this.roleType = roleType;
        this.authType = authType;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    //===수정 메서드===//
    /** Member 수정 */
    public void updateMember(UpdateMemberParam param) {
        this.name = param.getName();
        this.phoneNumber = param.getPhoneNumber();
        this.address = param.getAddress();
    }
}
