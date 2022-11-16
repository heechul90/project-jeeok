package com.jeeok.jeeokshop.core.item.dto;

import com.jeeok.jeeokshop.common.entity.Photo;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SaveItemParam {

    private String name;
    private int price;
    private int stockQuantity;
    private Photo photo;
}
