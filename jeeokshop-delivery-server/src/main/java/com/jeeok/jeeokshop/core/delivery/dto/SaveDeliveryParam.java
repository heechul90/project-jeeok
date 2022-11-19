package com.jeeok.jeeokshop.core.delivery.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveDeliveryParam {

    private Long memberId;
    private Long orderId;
}
