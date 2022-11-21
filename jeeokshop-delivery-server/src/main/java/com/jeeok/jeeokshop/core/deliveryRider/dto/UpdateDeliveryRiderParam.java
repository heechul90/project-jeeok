package com.jeeok.jeeokshop.core.deliveryRider.dto;

import com.jeeok.jeeokshop.common.entity.PhoneNumber;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateDeliveryRiderParam {

    private String riderName;
    private PhoneNumber phoneNumber;
}
