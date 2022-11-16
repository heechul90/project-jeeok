package com.jeeok.jeeokshop.core.delivery.dto;

import com.jeeok.jeeokshop.core.delivery.domain.Address;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateDeliveryParam {

    private Address address;
}
