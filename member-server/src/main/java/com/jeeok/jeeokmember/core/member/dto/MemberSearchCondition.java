package com.jeeok.jeeokmember.core.member.dto;

import com.jeeok.jeeokmember.common.dto.CommonSearchCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchCondition extends CommonSearchCondition {

    private SearchCondition searchCondition;
}
