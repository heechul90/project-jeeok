package com.jeeok.jeeokshop.core.delivery.dto;

import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliverySearchCondition {

    private DeliveryStatus searchDeliveryStatus;
}
