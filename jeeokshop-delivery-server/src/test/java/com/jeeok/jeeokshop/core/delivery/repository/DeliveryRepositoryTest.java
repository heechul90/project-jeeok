package com.jeeok.jeeokshop.core.delivery.repository;

import com.jeeok.jeeokshop.core.RepositoryTest;
import com.jeeok.jeeokshop.core.delivery.domain.Address;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.delivery.dto.DeliverySearchCondition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class DeliveryRepositoryTest extends RepositoryTest {

    //CREATE_DELIVERY
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final Address ADDRESS = new Address("38273", "서울시");

    @PersistenceContext protected EntityManager em;

    @Autowired protected DeliveryQueryRepository deliveryQueryRepository;

    private Delivery getDelivery(Address address, Long memberId, Long orderId) {
        return Delivery.createDelivery()
                .address(address)
                .memberId(memberId)
                .orderId(orderId)
                .build();
    }

    @Nested
    class successfulTest {

        @Test
        @DisplayName("배송 목록 조회")
        void findDeliveries() {
            //given
            IntStream.range(0, 15).forEach(i -> em.persist(getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1)));

            DeliverySearchCondition condition = new DeliverySearchCondition();
            condition.setSearchDeliveryStatus(DeliveryStatus.READY);

            PageRequest pageRequest = PageRequest.of(0, 10);

            //when
            Page<Delivery> content = deliveryQueryRepository.findDeliveries(condition, pageRequest);

            //then
            assertThat(content.getTotalElements()).isEqualTo(15);
            assertThat(content.getContent().size()).isEqualTo(10);
        }
    }
}