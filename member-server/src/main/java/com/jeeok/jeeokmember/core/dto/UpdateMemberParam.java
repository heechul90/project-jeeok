package com.jeeok.jeeokmember.core.dto;

import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMemberParam {

    private String memberName;
    private PhoneNumber phoneNumber;

}
