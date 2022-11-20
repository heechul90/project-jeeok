package com.jeeok.jeeokshop.core.store.controller.request;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.exception.JsonInvalidRequest;
import com.jeeok.jeeokshop.common.json.ErrorCode;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.dto.SaveStoreParam;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResisterStoreRequest {

    @NotBlank
    private String storeName;

    @NotBlank
    private String businessOpeningHours;
    @NotBlank
    private String businessClosingHours;

    @Length(min = 11, max = 11)
    private String phoneNumber;

    @Length(min = 5, max = 5)
    private String zipcode;
    @NotBlank
    private String address;

    @NotNull
    private List<ResisterStoreRequest.StoreCategoryRequest> storeCategories;

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class StoreCategoryRequest {
        @NotBlank
        private String categoryName;
        @NotNull
        private Integer categoryOrder;
    }

    //validate
    public void validate() {
        List<ErrorCode> errorCodes = new ArrayList<>();

        if (this.businessOpeningHours.length() != 4 || this.businessClosingHours.length() != 4) {
            errorCodes.add(new ErrorCode("businessHours", "business.hours", new Object[]{4}));
        }

        if (errorCodes.size() > 0) {
            throw new JsonInvalidRequest(errorCodes);
        }
    }

    public SaveStoreParam toParam(Long memberId) {
        return SaveStoreParam.builder()
                .name(this.storeName)
                .businessHours(new BusinessHours(this.businessOpeningHours, this.businessClosingHours))
                .phoneNumber(new PhoneNumber(this.phoneNumber.substring(0, 3), this.phoneNumber.substring(3, 7), this.phoneNumber.substring(7, 11)))
                .address(new Address(this.zipcode, this.address))
                .memberId(memberId)
                .storeCategoryParams(
                        this.storeCategories.stream()
                                .map(category -> SaveStoreParam.StoreCategoryParam.builder()
                                        .name(category.getCategoryName())
                                        .order(category.getCategoryOrder())
                                        .build()
                                ).collect(Collectors.toList())
                )
                .build();
    }
}
