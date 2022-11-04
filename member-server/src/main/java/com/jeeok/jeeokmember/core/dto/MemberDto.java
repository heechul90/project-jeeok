package com.jeeok.jeeokmember.core.dto;

import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.RoleType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long memberId;
    private String email;
    private String memberName;
    private RoleType role;
    private String phoneNumber;

    public MemberDto(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.memberName = member.getName();
        this.role = member.getRoleType();
        this.phoneNumber = member.getPhoneNumber().fullPhoneNumber();
    }
}
