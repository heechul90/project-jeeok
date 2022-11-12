package com.jeeok.jeeokshop.core.store.dto;

import com.jeeok.jeeokshop.common.dto.CommonSearchCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreSearchCondition extends CommonSearchCondition {

    private Long searchMemberId;

}
