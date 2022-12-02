package com.jeeok.jeeokshop.core.item.controller.response;

import com.jeeok.jeeokshop.core.item.domain.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindItemResponse {

    private Long itemId;
    private String itemName;
    private int price;

    private Long storeId;
    private String storeName;
    private String phoneNumber;

    private Long categoryId;
    private String categoryName;

    public FindItemResponse(Item item) {
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.price = item.getPrice();
        this.storeId = item.getStore().getId();
        this.storeName = item.getStore().getName();
        this.phoneNumber = item.getStore().getPhoneNumber().fullPhoneNumber();
        this.categoryId = item.getCategory().getId();
        this.categoryName = item.getCategory().getName();
    }
}
