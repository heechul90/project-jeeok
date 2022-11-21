package com.jeeok.jeeokshop.core.delivery.controller.response;

import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RiderFindDeliveryResponse {

    private Long deliveryId;
    private String address;
    private String deliveryStatus;
    private Long memberId;
    private Long orderId;

    public RiderFindDeliveryResponse(Delivery delivery) {
        this.deliveryId = delivery.getId();
        this.address = delivery.getAddress().fullAddress();
        this.deliveryStatus = delivery.getStatus().getMessage();
        this.memberId = delivery.getMemberId();
        this.orderId = delivery.getOrderId();
    }
}
