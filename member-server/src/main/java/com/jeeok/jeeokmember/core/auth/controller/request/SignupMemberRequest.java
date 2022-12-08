package com.jeeok.jeeokmember.core.auth.controller.request;

import com.jeeok.jeeokmember.common.entity.PhoneNumber;
import com.jeeok.jeeokmember.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokmember.common.json.ErrorCode;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.Member;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupMemberRequest {

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String memberName;

    @Length(min = 11, max = 11)
    private String phoneNumber;

    public Member toMember() {
        return Member.createMember()
                .email(this.email)
                .password(this.password)
                .name(this.memberName)
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
