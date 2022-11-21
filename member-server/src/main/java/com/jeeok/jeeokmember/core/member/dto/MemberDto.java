package com.jeeok.jeeokmember.core.member.dto;

import com.jeeok.jeeokmember.common.entity.Address;
import com.jeeok.jeeokmember.core.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDto {

    private Long memberId;
    private String email;
    private String memberName;
    private String role;
    private String auth;
    private String phoneNumber;
    private Address address;

    public MemberDto(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.memberName = member.getName();
        this.role = member.getRoleType().getTypeName();
        this.auth = member.getAuthType().getTypeName();
        this.phoneNumber = member.getPhoneNumber().fullPhoneNumber();
        this.address = member.getAddress();
    }
}
