package com.jeeok.jeeoklog.client.member;

import com.jeeok.jeeoklog.common.entity.Address;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FindMemberResponse {

    private Long memberId;
    private String email;
    private String memberName;
    private String role;
    private String auth;
    private String phoneNumber;
    private Address address;
}
