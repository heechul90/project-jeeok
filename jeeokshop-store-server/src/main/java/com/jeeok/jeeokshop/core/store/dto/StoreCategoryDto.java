package com.jeeok.jeeokshop.core.store.dto;

import com.jeeok.jeeokshop.core.category.domain.Category;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoreCategoryDto {

    private String categoryName;
    private Integer categoryOrder;

    public StoreCategoryDto(Category category) {
        this.categoryName = category.getName();
        this.categoryOrder = category.getOrder();
    }
}
