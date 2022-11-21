package com.jeeok.jeeokshop.core.deliveryRider.controller.response;

import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import lombok.*;

@Getter
PhoneNumber@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FindMyDeliveryRiderResponse {

    private Long deliveryRiderId;

    private Long riderId;
    private String riderName;
    private String phoneNumber;

    private Long deliveryId;
    private String deliveryAddress;
    private String deliveryStatus;

    private Long orderId;

    public FindMyDeliveryRiderResponse(DeliveryRider deliveryRider) {
        this.deliveryRiderId = deliveryRider.getId();
        this.riderId = deliveryRider.getRiderId();
        this.riderName = deliveryRider.getRiderName();
        this.phoneNumber = deliveryRider.getPhoneNumber().fullPhoneNumber();
        this.deliveryId = deliveryRider.getDelivery().getId();
        this.deliveryAddress = deliveryRider.getDelivery().getAddress().fullAddress();
        this.deliveryStatus = deliveryRider.getDelivery().getStatus().getMessage();
        this.orderId = deliveryRider.getDelivery().getOrderId();
    }
}
