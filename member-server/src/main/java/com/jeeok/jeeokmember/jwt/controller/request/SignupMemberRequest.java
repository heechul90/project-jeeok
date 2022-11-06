package com.jeeok.jeeokmember.jwt.controller.request;

import com.jeeok.jeeokmember.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokmember.common.json.ErrorCode;
import com.jeeok.jeeokmember.core.domain.AuthType;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.domain.RoleType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupMemberRequest {

    private String email;
    private String password;
    private String name;
    private String phoneNumber;

    public Member toMember() {
        return Member.createMember()
                .email(this.email)
                .password(this.password)
                .name(this.name)
                .roleType(RoleType.ROLE_USER)
                .authType(AuthType.JEEOK)
                .phoneNumber(new PhoneNumber(phoneNumber.substring(0, 3), phoneNumber.substring(3, 7), phoneNumber.substring(7, 11)))
                .build();
    }

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }
}
