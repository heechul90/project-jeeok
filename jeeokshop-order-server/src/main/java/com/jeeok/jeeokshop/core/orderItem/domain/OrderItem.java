package com.jeeok.jeeokshop.core.orderItem.domain;

import com.jeeok.jeeokshop.common.entity.BaseEntity;
import com.jeeok.jeeokshop.core.order.domain.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    private Long itemId;

    @Column(name = "orderPrice")
    private int price; //주문 가격
    @Column(name = "orderCount")
    private int count; //주문 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    //===연관과계 편의 메서드===//
    public void addOrder(Order order) {
        this.order = order;
//        order.getOrderItems().add(this);
    }

    //===생성 메서드===//
    public static OrderItem of(Long itemId, int price, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.itemId = itemId;
        orderItem.price = price;
        orderItem.count = count;
        return orderItem;
    }

    //===비즈니스 로직===//
    public void cancel() {

    }
}
