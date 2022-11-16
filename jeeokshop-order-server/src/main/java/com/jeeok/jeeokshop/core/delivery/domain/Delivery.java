package com.jeeok.jeeokshop.core.delivery.domain;

import com.jeeok.jeeokshop.core.delivery.dto.UpdateDeliveryParam;
import com.jeeok.jeeokshop.core.order.domain.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    //===생성 메서드===//
    /** 저장 */
    @Builder(builderMethodName = "createDelivery")
    public Delivery(Address address) {
        this.address = address;
        this.deliveryStatus = DeliveryStatus.READY;
    }

    //===수정===//
    public void updateDelivery(UpdateDeliveryParam param) {
        this.address = param.getAddress();
    }

    //===비즈니스 로직===//
    /** 배송 시작 */
    public void delivery() {
        this.deliveryStatus = DeliveryStatus.DELIVERY;
    }

    /** 배송 완료 */
    public void complete() {
        this.deliveryStatus = DeliveryStatus.COMPLETE;
    }

}
