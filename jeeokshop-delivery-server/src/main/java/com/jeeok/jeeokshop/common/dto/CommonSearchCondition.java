package com.jeeok.jeeokshop.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CommonSearchCondition {

    public SearchCondition searchCondition;
    public String searchKeyword;
}
