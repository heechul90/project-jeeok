package com.jeeok.jeeokshop.core.deliveryRider.controller.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateDeliveryRiderResponse {

    private Long updatedDeliveryRiderId;
}
