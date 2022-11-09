package com.jeeok.jeeokshop.core.store.domain;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.BaseEntity;
import com.jeeok.jeeokshop.common.entity.Photo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime businessStartTime;
    private LocalDateTime businessEndTime;

    private String phoneNumber;

    @Embedded
    private Address address;

    @Embedded
    private Photo photo;

    private Long memberId;

}
