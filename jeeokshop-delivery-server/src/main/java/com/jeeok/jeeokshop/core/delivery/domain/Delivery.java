package com.jeeok.jeeokshop.core.delivery.domain;

import com.jeeok.jeeokshop.common.entity.Address;
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

    private Long memberId;
    private Long orderId;

    //===연관관계 편의 메서드===//
    /*public void addDeliveryRider(DeliveryRider deliveryRider) {
        this.deliveryRider = deliveryRider;
    }*/

    //===생성 메서드===//
    @Builder(builderMethodName = "createDelivery")
    public Delivery(Address address, Long memberId, Long orderId) {
        this.address = address;
        this.status = DeliveryStatus.READY;
        this.memberId = memberId;
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

    /** 배달취소 */
    public void cancel() {
        this.status = DeliveryStatus.CANCEL;
    }
}
