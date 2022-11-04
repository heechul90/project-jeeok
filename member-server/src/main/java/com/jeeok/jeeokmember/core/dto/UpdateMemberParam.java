package com.jeeok.jeeokmember.core.dto;

import com.jeeok.jeeokmember.core.domain.PhoneNumber;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateMemberParam {

    private String name;
    private PhoneNumber phoneNumber;
}
