package com.jeeok.jeeokshop.core.order.repository;

import com.jeeok.jeeokshop.core.order.RepositoryTest;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.dto.OrderSearchCondition;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

class OrderRepositoryTest extends RepositoryTest {


    //CREATE_ORDER
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long MEMBER_ID_2 = 2L;

    @PersistenceContext protected EntityManager em;

    @Autowired protected OrderQueryRepository orderQueryRepository;

    @Autowired protected OrderRepository orderRepository;

    private List<OrderItem> getOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        IntStream.range(0, 2).forEach(i -> orderItems.add(OrderItem.of(Long.valueOf(i), 1000, 1)));
        return orderItems;
    }

    private Order getOrder(long memberId) {
        return Order.createOrder()
                .memberId(memberId)
                .orderItems(getOrderItems())
                .build();
    }

    @Test
    @DisplayName("주문 목록 조회")
    void findOrders() {
        //given
        IntStream.range(0, 16).forEach(i -> em.persist(getOrder(i % 2 == 0 ? MEMBER_ID_1 : MEMBER_ID_2)));

        OrderSearchCondition condition = new OrderSearchCondition();
        condition.setSearchMemberId(MEMBER_ID_1);

        PageRequest pageRequest = PageRequest.of(0, 10);

        //when
        Page<Order> content = orderQueryRepository.findOrders(condition, pageRequest);

        //then
        assertThat(content.getTotalElements()).isEqualTo(8);
        assertThat(content.getContent().size()).isEqualTo(8);
        assertThat(content.getContent()).extracting("memberId").containsExactly(MEMBER_ID_1);
    }
}