package com.jeeok.jeeokshop.core.store.dto;

import com.jeeok.jeeokshop.core.store.domain.Store;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoreDto {

    private Long storeId;
    private String storeName;

    private String businessOpeningHours;
    private String businessClosingHours;
    private String phoneNumber;
    private String address;

    private List<StoreCategoryDto> storeCategories;

    public StoreDto(Store store) {
        this.storeId = store.getId();
        this.storeName = store.getName();
        this.businessOpeningHours = store.getBusinessHours().getOpeningHours();
        this.businessClosingHours = store.getBusinessHours().getClosingHours();
        this.phoneNumber = store.getPhoneNumber().fullPhoneNumber();
        this.address = store.getAddress().fullAddress();
        this.storeCategories = store.getCategories().size() > 0 ? store.getCategories().stream()
                .map(StoreCategoryDto::new)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }
}
