package com.jeeok.jeeokmember.core.domain;

import com.jeeok.jeeokmember.common.entity.BaseEntity;
import com.jeeok.jeeokmember.core.dto.UpdateMemberParam;
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
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Embedded
    private PhoneNumber phoneNumber;

    //===생성자 메서드===//
    /** Member 생성 */
    @Builder(builderMethodName = "createMember")
    public Member(String email, String password, String name, RoleType roleType, PhoneNumber phoneNumber) {
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.name = name;
        this.roleType = roleType;
        this.phoneNumber = phoneNumber;
    }

    //===수정 메서드===//
    /** Member 수정 */
    public void updateMember(UpdateMemberParam param) {
        this.name = param.getMemberName();
        this.phoneNumber = param.getPhoneNumber();
    }

}
