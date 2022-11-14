package com.jeeok.jeeokshop.core.item.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateItemResponse {

    private Long updatedItemId;
}
