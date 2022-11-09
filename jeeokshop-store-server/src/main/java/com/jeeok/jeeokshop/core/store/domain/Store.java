package com.jeeok.jeeokshop.core.store.domain;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.BaseEntity;
import com.jeeok.jeeokshop.core.store.dto.UpdateStoreParam;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(name = "store_name")
    private String name;

    @Embedded
    private BusinessHours businessHours;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Address address;

    private Long memberId;

    //===생성===//
    //Store 생성
    @Builder(builderMethodName = "createStore")
    public Store(String name, BusinessHours businessHours, PhoneNumber phoneNumber, Address address, Long memberId) {
        this.name = name;
        this.businessHours = businessHours;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.memberId = memberId;
    }

    //===수정===//
    //Store 수정
    public void updateStore(UpdateStoreParam param) {
        this.name = param.getName();
        this.businessHours = param.getBusinessHours();
        this.phoneNumber = param.getPhoneNumber();
        this.address = param.getAddress();
    }
}
