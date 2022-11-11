package com.jeeok.jeeokshop.core.category.dto;

import com.jeeok.jeeokshop.core.category.domain.Category;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long categoryId;
    private String categoryName;
    private Integer order;

    public CategoryDto(Category category) {
        this.categoryId = category.getId();
        this.categoryName = category.getName();
        this.order = category.getOrder();
    }
}
