package com.jeeok.jeeokshop.client.store;

import lombok.*;

@Getter
@Setter
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
}
