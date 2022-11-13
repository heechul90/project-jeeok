package com.jeeok.jeeokshop.core.item.dto;

import com.jeeok.jeeokshop.common.entity.Yn;
import com.jeeok.jeeokshop.core.item.domain.Item;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ItemDto {

    private Long itemId;

    private String itemName;
    private Yn salesYn;
    private Long price;
    private String photo;

    public ItemDto(Item item) {
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.salesYn = item.getSalesYn();
        this.price = item.getPrice();
//        this.photo = item.getPhoto();
    }
}
