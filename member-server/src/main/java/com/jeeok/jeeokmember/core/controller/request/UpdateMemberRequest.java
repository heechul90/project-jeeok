package com.jeeok.jeeokmember.core.controller.request;

import com.jeeok.jeeokmember.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokmember.common.json.ErrorCode;
import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import com.jeeok.jeeokmember.core.dto.UpdateMemberParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateMemberRequest {

    private String memberName;
    private String phoneNumber;

    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public UpdateMemberParam toParam() {
        return UpdateMemberParam.builder()
                .name(this.memberName)
                .phoneNumber(new PhoneNumber(this.phoneNumber.substring(0, 3), this.phoneNumber.substring(3, 7), this.phoneNumber.substring(7, 11)))
                .build();
    }
}
