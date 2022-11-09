package com.jeeok.jeeokmember.core.dto;

import com.jeeok.jeeokmember.core.domain.AuthType;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.domain.RoleType;
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


}
