package com.jeeok.jeeokshop.core.item.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveItemResponse {

    private Long savedItemId;
}
