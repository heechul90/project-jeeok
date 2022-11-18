package com.jeeok.jeeokshop.core.delivery.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateDeliveryResponse {

    private Long updatedDeliveryId;
}
