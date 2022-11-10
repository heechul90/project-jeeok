package com.jeeok.jeeokmember.core.member.controller.response;

import com.jeeok.jeeokmember.core.member.domain.Member;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberInfoResponse {

    private Long memberId;
    private String email;
    private String memberName;
    private String phoneNumber;

    public MemberInfoResponse(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.memberName = member.getName();
        this.phoneNumber = member.getPhoneNumber().fullPhoneNumber();
    }
}
