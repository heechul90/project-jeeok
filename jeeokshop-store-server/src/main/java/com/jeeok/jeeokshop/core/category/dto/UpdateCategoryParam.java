package com.jeeok.jeeokshop.core.category.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateCategoryParam {

    private String name;
    private Integer order;
}
