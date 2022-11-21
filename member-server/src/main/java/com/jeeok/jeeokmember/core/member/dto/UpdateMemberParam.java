package com.jeeok.jeeokmember.core.member.dto;

import com.jeeok.jeeokmember.common.entity.Address;
import com.jeeok.jeeokmember.common.entity.PhoneNumber;
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
