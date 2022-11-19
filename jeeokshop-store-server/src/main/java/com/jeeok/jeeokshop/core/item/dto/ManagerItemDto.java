package com.jeeok.jeeokshop.core.item.dto;

import com.jeeok.jeeokshop.common.entity.Yn;
import com.jeeok.jeeokshop.core.item.domain.Item;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ManagerItemDto {

    private Long itemId;

    private String itemName;
    private Yn salesYn;
    private int price;
    private int stockQuantity;
    private String photo;

    public ManagerItemDto(Item item) {
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.salesYn = item.getSalesYn();
        this.price = item.getPrice();
        this.stockQuantity = item.getStockQuantity();
        this.photo = item.getPhoto().fullPhotoPath();
    }
}
