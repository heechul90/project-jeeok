package com.jeeok.jeeokshop.client.member;

import com.jeeok.jeeokshop.core.delivery.domain.Address;
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
