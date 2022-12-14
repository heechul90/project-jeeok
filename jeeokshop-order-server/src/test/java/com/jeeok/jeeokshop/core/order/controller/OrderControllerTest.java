package com.jeeok.jeeokshop.core.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.order.controller.request.OrderRequest;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.domain.OrderStatus;
import com.jeeok.jeeokshop.core.order.dto.OrderSearchCondition;
import com.jeeok.jeeokshop.core.order.service.OrderService;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends IntegrationTest {

    //CREATE_ORDER
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long MEMBER_ID_2 = 2L;
    public static final Long ORDER_ID_1 = 1L;
    public static final OrderStatus ORDER_STATUS = OrderStatus.ORDER;

    //REQUEST_URL
    public static final String HEADER_NAME = "member-id";
    public static final String API_FIND_ORDERS = "/front/orders";
    public static final String API_FIND_ORDER = "/front/orders/{orderId}";
    public static final String API_ORDER = "/front/orders";
    public static final String API_CANCEL_ORDER = "/front/orders/{orderId}/cancel";
    public static final String API_DELETE_ORDER = "/front/orders/{orderId}";

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
    @DisplayName("?????? ??????")
    void findMyOrders() throws Exception {
        //given
        IntStream.range(0, 20).forEach(i -> em.persist(getOrder(i % 2 == 0 ? MEMBER_ID_1 : MEMBER_ID_2)));

        OrderSearchCondition condition = new OrderSearchCondition();

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_ORDERS)
                .header(HEADER_NAME, MEMBER_ID_1)
                .queryParams(conditionParams)
                .queryParams(pageRequestParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(10)))
                .andDo(print())
                .andDo(document("findOrders",
                        requestParameters(
                                parameterWithName("searchMemberId").ignored(),
                                parameterWithName("page").description("?????? ?????????"),
                                parameterWithName("size").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data[*].orderId").description("?????? ????????????"),
                                fieldWithPath("data[*].orderDate").description("?????? ??????"),
                                fieldWithPath("data[*].orderStatus").description("?????? ??????"),
                                fieldWithPath("data[*].orderItems[*].orderItemId").description("?????? ?????? ????????????"),
                                fieldWithPath("data[*].orderItems[*].itemId").description("?????? ????????????"),
                                fieldWithPath("data[*].orderItems[*].orderPrice").description("?????? ??????"),
                                fieldWithPath("data[*].orderItems[*].orderCount").description("?????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void findMyOrder() throws Exception {
        //given
        em.persist(order);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_ORDER, order.getId()));

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
                .andDo(document("findOrder",
                        pathParameters(
                                parameterWithName("orderId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.orderId").description("?????? ????????????"),
                                fieldWithPath("data.orderDate").description("?????? ??????"),
                                fieldWithPath("data.orderStatus").description("?????? ??????"),
                                fieldWithPath("data.orderItems[*].orderItemId").description("?????? ?????? ????????????"),
                                fieldWithPath("data.orderItems[*].itemId").description("?????? ????????????"),
                                fieldWithPath("data.orderItems[*].orderPrice").description("?????? ??????"),
                                fieldWithPath("data.orderItems[*].orderCount").description("?????? ??????")
                        )
                ));
    }

    @Test
    @DisplayName("??????")
    void order() throws Exception {
        //given
        OrderRequest request = OrderRequest.builder()
                .items(
                        getOrderItems().stream()
                                .map(orderItem -> OrderRequest.Item.builder()
                                        .itemId(orderItem.getItemId())
                                        .orderPrice(orderItem.getPrice())
                                        .orderCount(orderItem.getCount())
                                        .build()
                                ).collect(Collectors.toList())
                )
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_ORDER)
                .header(HEADER_NAME, MEMBER_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("order",
                        requestFields(
                                fieldWithPath("items[*].itemId").description("?????? ????????????"),
                                fieldWithPath("items[*].orderPrice").description("?????? ??????"),
                                fieldWithPath("items[*].orderCount").description("?????? ??????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.orderedId").description("????????? ?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void cancelOrder() throws Exception {
        //given
        em.persist(order);

        //when
        ResultActions resultActions = mockMvc.perform(put(API_CANCEL_ORDER, order.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("orderCancel",
                        pathParameters(
                                parameterWithName("orderId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").description("?????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
    void deleteOrder() throws Exception {
        //given
        em.persist(order);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_ORDER, order.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("deleteOrder",
                        pathParameters(
                                parameterWithName("orderId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").description("?????????")
                        )
                ));
    }
}