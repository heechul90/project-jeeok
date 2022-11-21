package com.jeeok.jeeokshop.core.deliveryRider.domain;

import com.jeeok.jeeokshop.common.entity.BaseEntity;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.deliveryRider.dto.UpdateDeliveryRiderParam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "delivery_rider")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryRider extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_rider_id")
    private Long id;

    private Long riderId;
    private String riderName;
    private String riderPhoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //===생성 메서드===//
    @Builder(builderMethodName = "createDeliveryRider")
    public DeliveryRider(Long riderId, String riderName, String riderPhoneNumber, Delivery delivery) {
        this.riderId = riderId;
        this.riderName = riderName;
        this.riderPhoneNumber = riderPhoneNumber;
        this.delivery = delivery;
    }

    //===수정 메서드===//
    public void updateDeliveryRider(UpdateDeliveryRiderParam param) {
        this.riderName = param.getRiderName();
        this.riderPhoneNumber = param.getRiderPhoneNumber();
    }
}
