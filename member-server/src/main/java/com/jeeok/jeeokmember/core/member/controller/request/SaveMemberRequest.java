package com.jeeok.jeeokmember.core.member.controller.request;

import com.jeeok.jeeokmember.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokmember.common.json.ErrorCode;
import com.jeeok.jeeokmember.core.member.domain.Address;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import com.jeeok.jeeokmember.core.member.dto.SaveMemberParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveMemberRequest {

    private String email;
    private String password;
    private String memberName;
    private RoleType role;
    private String phoneNumber;
    private String zipcode;
    private String address;

    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public SaveMemberParam toParam() {
        return SaveMemberParam.builder()
                .email(this.email)
                .password(this.password)
                .name(this.memberName)
                .roleType(this.role)
                .authType(AuthType.JEEOK)
                .phoneNumber(new PhoneNumber(this.phoneNumber.substring(0, 3), this.phoneNumber.substring(3, 7), this.phoneNumber.substring(7, 11)))
                .address(new Address(this.zipcode, this.address))
                .build();
    }
}
