package com.jeeok.jeeokshop.core.order.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.MockTest;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.domain.OrderStatus;
import com.jeeok.jeeokshop.core.order.dto.OrderParam;
import com.jeeok.jeeokshop.core.order.repository.OrderRepository;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OrderServiceTest extends MockTest {

    //CREATE_ORDER
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final OrderStatus ORDER_STATUS = OrderStatus.ORDER;

    //ERORR_MESSAGE
    public static final String ORDER = "Order";
    public static final Long NOT_FOUND_ORDER_ID_0 = 0L;
    public static final String HAS_MESSAGE_STARTING_WITH = "존재하지 않는 ";
    public static final String HAS_MESSAGE_ENDING_WITH = "id=";

    @Mock protected OrderRepository orderRepository;

    @InjectMocks protected OrderService orderService;

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
        @DisplayName("상품 단건 조회")
        void findOrder() {
            //given
            given(orderRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(order));

            //when
            Order findOrder = orderService.findOrder(ORDER_ID_1);

            //then
            assertThat(findOrder.getMemberId()).isEqualTo(MEMBER_ID_1);
            assertThat(findOrder.getStatus()).isEqualTo(ORDER_STATUS);
            assertThat(findOrder.getOrderItems().size()).isEqualTo(2);

            //verify
            verify(orderRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("상품 저장")
        void saveOrder() {
            //given
            given(orderRepository.save(any(Order.class))).willReturn(order);

            OrderParam param = OrderParam.builder()
                    .items(
                            order.getOrderItems().stream()
                                    .map(orderItem -> OrderParam.Item.builder()
                                            .itemId(orderItem.getItemId())
                                            .price(orderItem.getPrice())
                                            .count(orderItem.getCount())
                                            .build()
                                    ).collect(Collectors.toList())
                    )
                    .build();

            //when
            Order savedOrder = orderService.saveOrder(MEMBER_ID_1, param);

            //then
            assertThat(savedOrder.getMemberId()).isEqualTo(MEMBER_ID_1);
            assertThat(savedOrder.getStatus()).isEqualTo(ORDER_STATUS);
            assertThat(savedOrder.getOrderItems().size()).isEqualTo(2);

            //verify
            verify(orderRepository, times(1)).save(any(Order.class));
        }

        @Test
        @DisplayName("상품 취소")
        void cancelOrder() {
            //given
            given(orderRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(order));

            //when
            orderService.cancelOrder(ORDER_ID_1);

            //then
            assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);

            //verify
            verify(orderRepository, times(1)).findById(any(Long.class));
        }

        @Test
        @DisplayName("상품 삭제")
        void deleteOrder() {
            //given
            given(orderRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(order));

            //when
            orderService.deleteOrder(ORDER_ID_1);

            //then

            //verify
            verify(orderRepository, times(1)).findById(any(Long.class));
            verify(orderRepository, times(1)).delete(any(Order.class));
        }
    }

    @Nested
    class UnsuccessfulTest {

        @Test
        @DisplayName("주문 단건 조회_entityNotFound")
        void findOrder_entityNotfound() {
            //given
            given(orderRepository.findById(any(Long.class))).willThrow(new EntityNotFound(ORDER, NOT_FOUND_ORDER_ID_0));

            //expected
            assertThatThrownBy(() -> orderService.findOrder(NOT_FOUND_ORDER_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + ORDER)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ORDER_ID_0);
        }

        @Test
        @DisplayName("주문 취소_entityNotFound")
        void cancelOrder_entityNotfound() {
            //given
            given(orderRepository.findById(any(Long.class))).willThrow(new EntityNotFound(ORDER, NOT_FOUND_ORDER_ID_0));

            //expected
            assertThatThrownBy(() -> orderService.cancelOrder(NOT_FOUND_ORDER_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + ORDER)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ORDER_ID_0);
        }

        @Test
        @DisplayName("주문 삭제_entityNotFound")
        void deleteOrder_entityNotfound() {
            //given
            given(orderRepository.findById(any(Long.class))).willThrow(new EntityNotFound(ORDER, NOT_FOUND_ORDER_ID_0));

            //expected
            assertThatThrownBy(() -> orderService.deleteOrder(NOT_FOUND_ORDER_ID_0))
                    .isInstanceOf(EntityNotFound.class)
                    .hasMessageStartingWith(HAS_MESSAGE_STARTING_WITH + ORDER)
                    .hasMessageEndingWith(HAS_MESSAGE_ENDING_WITH + NOT_FOUND_ORDER_ID_0);
        }
    }
}
