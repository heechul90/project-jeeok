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
public class SaveStoreParam {

    private String name;

    private BusinessHours businessHours;
    private PhoneNumber phoneNumber;
    private Address address;
    private Long memberId;

    private List<StoreCategoryParam> storeCategoryParams;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class StoreCategoryParam {
        private String name;
        private Integer order;
    }
}
