package com.jeeok.jeeokshop.core.category.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveCategoryParam {

    private String name;
    private Integer order;
    private Long storeId;
}
