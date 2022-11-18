package com.jeeok.jeeokshop.core.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.order.IntegrationTest;
import com.jeeok.jeeokshop.core.order.controller.request.OrderRequest;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.domain.OrderStatus;
import com.jeeok.jeeokshop.core.order.dto.OrderSearchCondition;
import com.jeeok.jeeokshop.core.order.service.OrderService;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FrontOrderControllerTest extends IntegrationTest {

    //CREATE_ORDER
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long MEMBER_ID_2 = 2L;
    public static final Long ORDER_ID_1 = 1L;
    public static final OrderStatus ORDER_STATUS = OrderStatus.ORDER;

    //ERORR_MESSAGE
    public static final String ORDER = "Order";
    public static final Long NOT_FOUND_ORDER_ID_0 = 0L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    //REQUEST_URL
    public static final String HEADER_NAME = "member-id";
    public static final String FRONT_FIND_ORDERS = "/front/orders";
    public static final String FRONT_FIND_ORDER = "/front/orders/{orderId}";
    public static final String FRONT_ORDER = "/front/orders";
    public static final String FRONT_CANCEL_ORDER = "/front/orders/{orderId}/cancel";
    public static final String FRONT_DELETE_ORDER = "/front/orders/{orderId}";

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

    @Nested
    class successfulTest {

        @Test
        @DisplayName("주문 목록")
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
            ResultActions resultActions = mockMvc.perform(get(FRONT_FIND_ORDERS)
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
                    .andDo(document("front-findOrders",
                            requestParameters(
                                    parameterWithName("searchMemberId").ignored(),
                                    parameterWithName("page").description("검색 페이지"),
                                    parameterWithName("size").description("검색 사이즈")
                            ),
                            responseFields(
                                    fieldWithPath("transaction_time").description("api 요청 시간"),
                                    fieldWithPath("code").description("SUCCESS or ERROR"),
                                    fieldWithPath("message").description("메시지"),
                                    fieldWithPath("errors").description("에러"),
                                    fieldWithPath("data[*].orderId").description("주문 고유번호"),
                                    fieldWithPath("data[*].orderDate").description("주문 날짜"),
                                    fieldWithPath("data[*].orderStatus").description("주문 상태"),
                                    fieldWithPath("data[*].orderItems[*].orderItemId").description("주문 상품 고유번호"),
                                    fieldWithPath("data[*].orderItems[*].itemId").description("상품 고유번호"),
                                    fieldWithPath("data[*].orderItems[*].orderPrice").description("주문 가격"),
                                    fieldWithPath("data[*].orderItems[*].orderCount").description("주문 수량")
                            )
                    ));
        }

        @Test
        @DisplayName("주문 상세")
        void findMyOrder() throws Exception {
            //given
            em.persist(order);

            //when
            ResultActions resultActions = mockMvc.perform(get(FRONT_FIND_ORDER, ORDER_ID_1));

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
                    .andDo(document("front-findOrder",
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
            ResultActions resultActions = mockMvc.perform(post(FRONT_ORDER)
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
                    .andDo(document("front-order",
                            requestFields(
                                    fieldWithPath("items[*].itemId").description("상품 고유번호"),
                                    fieldWithPath("items[*].orderPrice").description("주문 가격"),
                                    fieldWithPath("items[*].orderCount").description("주문 수량")
                            ),
                            responseFields(
                                    fieldWithPath("transaction_time").description("api 요청 시간"),
                                    fieldWithPath("code").description("SUCCESS or ERROR"),
                                    fieldWithPath("message").description("메시지"),
                                    fieldWithPath("errors").description("에러"),
                                    fieldWithPath("data.orderedId").description("주문된 주문 고유번호")
                            )
                    ));
        }

        @Test
        @DisplayName("주문 취소")
        void cancelOrder() throws Exception {
            //given
            em.persist(order);

            //when
            ResultActions resultActions = mockMvc.perform(put(FRONT_CANCEL_ORDER, ORDER_ID_1));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                    .andExpect(jsonPath("$.message").isEmpty())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andDo(print())
                    .andDo(document("front-orderCancel",
                            pathParameters(
                                    parameterWithName("orderId").description("주문 고유번호")
                            ),
                            responseFields(
                                    fieldWithPath("transaction_time").description("api 요청 시간"),
                                    fieldWithPath("code").description("SUCCESS or ERROR"),
                                    fieldWithPath("message").description("메시지"),
                                    fieldWithPath("errors").description("에러"),
                                    fieldWithPath("data").description("데이터")
                            )
                    ));
        }

        @Test
        @DisplayName("주문 삭제")
        void deleteOrder() throws Exception {
            //given
            em.persist(order);

            //when
            ResultActions resultActions = mockMvc.perform(delete(FRONT_DELETE_ORDER, ORDER_ID_1));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                    .andExpect(jsonPath("$.message").isEmpty())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andDo(print())
                    .andDo(document("front-deleteOrder",
                            pathParameters(
                                    parameterWithName("orderId").description("주문 고유번호")
                            ),
                            responseFields(
                                    fieldWithPath("transaction_time").description("api 요청 시간"),
                                    fieldWithPath("code").description("SUCCESS or ERROR"),
                                    fieldWithPath("message").description("메시지"),
                                    fieldWithPath("errors").description("에러"),
                                    fieldWithPath("data").description("데이터")
                            )
                    ));
        }
    }
}