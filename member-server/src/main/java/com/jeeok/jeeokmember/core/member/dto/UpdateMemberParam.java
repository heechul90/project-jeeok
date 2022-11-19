package com.jeeok.jeeokmember.core.member.dto;

import com.jeeok.jeeokmember.core.member.domain.Address;
import com.jeeok.jeeokmember.core.member.domain.PhoneNumber;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateMemberParam {

    private String name;
    private PhoneNumber phoneNumber;
    private Address address;
}
