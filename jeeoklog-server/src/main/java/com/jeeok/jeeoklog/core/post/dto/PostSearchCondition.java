package com.jeeok.jeeoklog.core.post.dto;

import com.jeeok.jeeoklog.common.dto.CommonSearchCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchCondition extends CommonSearchCondition {

    private SearchCondition searchCondition;
}
