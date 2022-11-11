package com.jeeok.jeeokshop.core.store.dto;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateStoreParam {

    private String name;

    private BusinessHours businessHours;
    private PhoneNumber phoneNumber;
    private Address address;

    private List<StoreCategoryParam> storeCategoryParams;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class StoreCategoryParam {
        private Long categoryId;
        private String name;
        private Integer order;
    }
}
