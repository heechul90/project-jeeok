package com.jeeok.jeeokshop.core.delivery.dto;

import com.jeeok.jeeokshop.common.entity.Address;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateDeliveryParam {

    private Address address;
}
