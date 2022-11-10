package com.jeeok.jeeokmember.core.member.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberEditResponse {

    private Long memberId;
}
