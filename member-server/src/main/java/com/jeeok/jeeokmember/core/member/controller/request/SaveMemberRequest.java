package com.jeeok.jeeokmember.core.member.controller.request;

import com.jeeok.jeeokmember.common.entity.Address;
import com.jeeok.jeeokmember.common.entity.PhoneNumber;
import com.jeeok.jeeokmember.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokmember.common.json.ErrorCode;
import com.jeeok.jeeokmember.core.member.domain.AuthType;
import com.jeeok.jeeokmember.core.member.domain.RoleType;
import com.jeeok.jeeokmember.core.member.dto.SaveMemberParam;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveMemberRequest {

    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String memberName;
    @NotNull
    private RoleType roleType;
    @Length(min = 11, max = 11)
    private String phoneNumber;
    @Length(min = 5, max = 5)
    private String zipcode;
    @NotBlank
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
                .roleType(this.roleType)
                .authType(AuthType.JEEOK)
                .phoneNumber(new PhoneNumber(this.phoneNumber.substring(0, 3), this.phoneNumber.substring(3, 7), this.phoneNumber.substring(7, 11)))
                .address(new Address(this.zipcode, this.address))
                .build();
    }
}
