package com.jeeok.jeeokmember.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchCondition {

    private SearchCondition searchCondition;
    private String searchKeyword;
}
