package com.jeeok.jeeokshop.core.itemoption.domain;

import com.jeeok.jeeokshop.common.entity.BaseEntity;
import com.jeeok.jeeokshop.core.item.domain.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOption extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_option_id")
    private Long id;

    @Column(name = "item_option_name")
    private String name;

    private OptionType optionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

}
