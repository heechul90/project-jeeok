package com.jeeok.jeeokshop.core.deliveryRider.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveDeliveryRiderParam {

    private Long deliveryId;
}
