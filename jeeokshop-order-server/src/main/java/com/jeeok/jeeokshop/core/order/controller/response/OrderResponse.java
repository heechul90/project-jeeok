package com.jeeok.jeeokshop.core.order.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderedId;
}
