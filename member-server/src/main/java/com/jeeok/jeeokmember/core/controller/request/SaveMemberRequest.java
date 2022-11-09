package com.jeeok.jeeokmember.core.controller.request;

import com.jeeok.jeeokmember.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokmember.common.json.ErrorCode;
import com.jeeok.jeeokmember.core.domain.AuthType;
import com.jeeok.jeeokmember.core.domain.Member;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.domain.RoleType;
import com.jeeok.jeeokmember.core.dto.SaveMemberParam;
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
                .build();
    }
}
