package com.jeeok.jeeokshop.core.category.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateCategoryResponse {

    private Long updatedCategoryId;
}
