package com.jeeok.jeeokshop.core.delivery.domain;

import com.jeeok.jeeokshop.core.delivery.dto.UpdateDeliveryParam;
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
    @Column(name = "delivery_status")
    private DeliveryStatus status;

    private Long orderId;

    //===생성 메서드===//
    @Builder(builderMethodName = "createDelivery")
    public Delivery(Address address, Long orderId) {
        this.address = address;
        this.status = DeliveryStatus.READY;
        this.orderId = orderId;
    }

    //===수정 메서드===//
    public void updateDelivery(UpdateDeliveryParam param) {
        this.address = param.getAddress();
    }

    //===비즈니스 로직===//
    /** 배달시작 */
    public void delivery() {
        this.status = DeliveryStatus.DELIVERY;
    }

    /** 배달완료 */
    public void complete() {
        this.status = DeliveryStatus.COMPLETE;
    }
}
