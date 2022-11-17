package com.jeeok.jeeokshop.core.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.domain.OrderStatus;
import com.jeeok.jeeokshop.core.order.service.OrderService;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class FrontOrderControllerTest {

    //CREATE_ORDER
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final OrderStatus ORDER_STATUS = OrderStatus.ORDER;

    //ERORR_MESSAGE
    public static final String ORDER = "Order";
    public static final Long NOT_FOUND_ORDER_ID_0 = 0L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper objectMapper;

    @PersistenceContext protected EntityManager em;

    @Autowired protected OrderService orderService;

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

    Order order;

    @BeforeEach
    void beforeEach() {
        order = getOrder(MEMBER_ID_1);
    }

    @Test
    void findMyOrders() {
    }

    @Test
    void findMyOrder() {
    }

    @Test
    void order() {
    }

    @Test
    void cancelOrder() {
    }

    @Test
    void deleteOrder() {
    }
}