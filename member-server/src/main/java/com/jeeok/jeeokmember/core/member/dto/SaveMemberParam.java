package com.jeeok.jeeokmember.core.member.dto;

import com.jeeok.jeeokmember.core.member.domain.Address;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveMemberParam {

    private String email;
    private String password;
    private String name;
    private RoleType roleType;
    private AuthType authType;
    private PhoneNumber phoneNumber;
    private Address address;
}
