package com.jeeok.jeeokshop.core.order.domain;

import com.jeeok.jeeokshop.common.entity.BaseEntity;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private Long memberId;

    private LocalDateTime orderTime;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //===연관관계 편의 메서드===//


    //===생성===//
    @Builder(builderMethodName = "createOrder")
    public Order(Long memberId, List<OrderItem> orderItems) {
        this.memberId = memberId;
        this.orderTime = LocalDateTime.now();
        this.orderStatus = OrderStatus.ORDER;

        //최소 한개의 item 을 주문(controller 에서 check 할 것)
        this.orderItems.forEach(orderItem -> orderItem.addOrder(this));
    }

    //===비즈니스 로직===//
    /** 주문취소 */
    public void cancel() {
        this.orderStatus = OrderStatus.CANCEL;
        this.orderItems.forEach(OrderItem::cancel);
    }
}
