package com.jeeok.jeeokshop.core.store.dto;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateStoreParam {

    private String name;

    private BusinessHours businessHours;
    private PhoneNumber phoneNumber;
    private Address address;

}
