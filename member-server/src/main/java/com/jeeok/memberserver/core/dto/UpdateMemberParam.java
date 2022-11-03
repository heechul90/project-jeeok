package com.jeeok.memberserver.core.dto;

import com.jeeok.memberserver.core.domain.PhoneNumber;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMemberParam {

    private String memberName;
    private PhoneNumber phoneNumber;
}
