package com.jeeok.jeeokshop.core.category.dto;

import com.jeeok.jeeokshop.common.dto.CommonSearchCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySearchCondition extends CommonSearchCondition {

    private Long searchMemberId;
}
