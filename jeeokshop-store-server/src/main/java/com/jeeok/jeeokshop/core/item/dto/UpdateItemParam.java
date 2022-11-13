package com.jeeok.jeeokshop.core.item.dto;

import com.jeeok.jeeokshop.common.entity.Photo;
import com.jeeok.jeeokshop.common.entity.Yn;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UpdateItemParam {

    private String name;
    private Yn yn;
    private int price;
    private Photo photo;
}
