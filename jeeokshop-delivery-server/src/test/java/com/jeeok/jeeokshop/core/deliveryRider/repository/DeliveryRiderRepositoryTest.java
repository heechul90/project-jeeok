package com.jeeok.jeeokshop.core.deliveryRider.repository;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.PhoneNumber;
import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import com.jeeok.jeeokshop.core.deliveryRider.dto.DeliveryRiderSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryRiderRepositoryTest extends RepositoryTest {

    //CREATE_DELIVERY_RIDER
    public static final Address DELIVERY_ASSRESS = new Address("00283", "서울시");
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final Long RIDER_ID_1 = 7L;
    public static final String RIDER_NAME = "rider_name";
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "2203", "3838");
    public static final Long DELIVERY_RIDER_ID_1 = 1L;
    public static final Long DELIVERY_ID_1 = 1L;

    @PersistenceContext protected EntityManager em;
    @Autowired protected DeliveryRiderQueryRepository deliveryRiderQueryRepository;

    @Test
    void findRiders() {
        //given
        IntStream.range(0, 15).forEach(i -> {
            Delivery delivery = Delivery.createDelivery()
                    .address(DELIVERY_ASSRESS)
                    .memberId(MEMBER_ID_1)
                    .orderId(ORDER_ID_1 + i)
                    .build();
            em.persist(delivery);
            DeliveryRider deliveryRider = DeliveryRider.createDeliveryRider()
                    .riderId(RIDER_ID_1 + i)
                    .riderName(RIDER_NAME + i)
                    .phoneNumber(PHONE_NUMBER)
                    .delivery(delivery)
                    .build();
            em.persist(deliveryRider);
        });

        DeliveryRiderSearchCondition condition = new DeliveryRiderSearchCondition();
        condition.setSearchRiderId(RIDER_ID_1);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<DeliveryRider> content = deliveryRiderQueryRepository.findDeliveryRiders(condition, pageRequest);

        //then
        assertThat(content.getTotalElements()).isEqualTo(1);
        assertThat(content.getContent().size()).isEqualTo(1);
    }
}