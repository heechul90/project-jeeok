package com.jeeok.jeeokshop.core.category.domain;

import com.jeeok.jeeokshop.common.entity.BaseEntity;
import com.jeeok.jeeokshop.core.category.dto.UpdateCategoryParam;
import com.jeeok.jeeokshop.core.store.domain.Store;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String name;

    @Column(name = "category_order")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    public Store store;

    //===생성===//
    @Builder(builderMethodName = "createCategory")
    public Category(String name, Integer order, Store store) {
        this.name = name;
        this.order = order;
        this.store = store;
    }

    //===수정===//
    public void updateCategory(UpdateCategoryParam param) {
        this.name = param.getName();
        this.order = param.getOrder();
    }

    //===연관관계 편의 메서드===//
    public void addStore(Store store) {
        this.store = store;
    }
}
