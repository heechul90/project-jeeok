package com.jeeok.jeeokshop.core.item.domain;

import com.jeeok.jeeokshop.common.entity.BaseEntity;
import com.jeeok.jeeokshop.common.entity.Photo;
import com.jeeok.jeeokshop.common.entity.Yn;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.item.dto.UpdateItemParam;
import com.jeeok.jeeokshop.core.store.domain.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "item_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private Yn salesYn;

    private int price;

    @Embedded
    private Photo photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    //===생성===//
    @Builder(builderMethodName = "createItem")
    public Item(String name, int price, Photo photo, Store store, Category category) {
        this.name = name;
        this.salesYn = Yn.Y;
        this.price = price;
        this.photo = photo;
        this.store = store;
        this.category = category;
    }

    //===수정===//
    public void updateItem(UpdateItemParam param) {
        this.name = param.getName();
        this.salesYn = param.getYn();
        this.price = param.getPrice();
        this.photo = param.getPhoto();
    }
}
