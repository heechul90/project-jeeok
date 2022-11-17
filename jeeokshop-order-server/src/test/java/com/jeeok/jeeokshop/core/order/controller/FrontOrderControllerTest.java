package com.jeeok.jeeokshop.core.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.order.IntegrationTest;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.domain.OrderStatus;
import com.jeeok.jeeokshop.core.order.service.OrderService;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FrontOrderControllerTest extends IntegrationTest {

    //CREATE_ORDER
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final OrderStatus ORDER_STATUS = OrderStatus.ORDER;

    //ERORR_MESSAGE
    public static final String ORDER = "Order";
    public static final Long NOT_FOUND_ORDER_ID_0 = 0L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";
    public static final String FRONT_FIND_ORDER = "/front/orders/{orderId}";

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
    @DisplayName("주문 목록")
    void findMyOrders() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("주문 상세")
    void findMyOrder() throws Exception {
        //given
        em.persist(order);

        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(FRONT_FIND_ORDER, ORDER_ID_1));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.orderId").value(order.getId()))
                .andExpect(jsonPath("$.data.orderStatus").value(ORDER_STATUS.getDescription()))
                .andExpect(jsonPath("$.data.orderItems.length()", Matchers.is(2)))
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("front-findOrder",
                        pathParameters(
                                parameterWithName("orderId").description("주문 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.orderId").description("주문 고유번호"),
                                fieldWithPath("data.orderDate").description("주문 날짜"),
                                fieldWithPath("data.orderStatus").description("주문 상태"),
                                fieldWithPath("data.orderItems[*].orderItemId").description("주문 상품 고유번호"),
                                fieldWithPath("data.orderItems[*].itemId").description("상품 고유번호"),
                                fieldWithPath("data.orderItems[*].orderPrice").description("주문 가격"),
                                fieldWithPath("data.orderItems[*].orderCount").description("주문 수량")
                        )
                ));
    }

    @Test
    @DisplayName("주문")
    void order() throws Exception {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("주문 취소")
    void cancelOrder() {
        //given

        //when

        //then
    }

    @Test
    @DisplayName("주문 삭제")
    void deleteOrder() {
        //given

        //when

        //then
    }
}