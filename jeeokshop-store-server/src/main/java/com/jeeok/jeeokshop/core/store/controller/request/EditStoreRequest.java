package com.jeeok.jeeokshop.core.store.controller.request;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.dto.UpdateStoreParam;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EditStoreRequest {

    private String storeName;

    private String businessOpeningHours;
    private String businessClosingHours;

    private String phoneNumber;

    private String zipcode;
    private String address;

    private List<EditStoreRequest.UpdateStoreCategory> storeCategories;

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UpdateStoreCategory {
        private Long categoryId;
        private String categoryName;
        private Integer categoryOrder;
    }

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public UpdateStoreParam toParam() {
        return UpdateStoreParam.builder()
                .name(this.storeName)
                .businessHours(new BusinessHours(this.businessOpeningHours, this.businessClosingHours))
                .phoneNumber(new PhoneNumber(this.phoneNumber.substring(0, 3), this.phoneNumber.substring(3, 7), this.phoneNumber.substring(7, 11)))
                .address(new Address(this.zipcode, this.address))
                .storeCategoryParams(
                        this.storeCategories.stream()
                                .map(catetory -> UpdateStoreParam.StoreCategoryParam.builder()
                                        .categoryId(catetory.getCategoryId())
                                        .name(catetory.getCategoryName())
                                        .order(catetory.getCategoryOrder())
                                        .build()
                                ).collect(Collectors.toList())
                )
                .build();
    }
}
