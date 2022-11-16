package com.jeeok.jeeokshop.core.order.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.dto.OrderSearchCondition;
import com.jeeok.jeeokshop.core.order.dto.SaveOrderParam;
import com.jeeok.jeeokshop.core.order.repository.OrderQueryRepository;
import com.jeeok.jeeokshop.core.order.repository.OrderRepository;
import com.jeeok.jeeokshop.core.orderItem.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    public static final String ORDER = "Order";

    private final OrderQueryRepository orderQueryRepository;
    private final OrderRepository orderRepository;

    /**
     * 주문 목록 조회
     */
    public Page<Order> findOrders(OrderSearchCondition condition, Pageable pageable) {
        return orderQueryRepository.findOrders(condition, pageable);
    }

    /**
     * 주문 단건 조회
     */
    public Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFound(ORDER, orderId));
    }

    /**
     * 주문 저장
     */
    @Transactional
    public Order saveOrder(Long memberId, SaveOrderParam param) {

        List<OrderItem> orderItems = param.getItems().stream()
                .map(item -> OrderItem.of(item.getItemId(), item.getPrice(), item.getCount()))
                .collect(Collectors.toList());

        Order order = Order.createOrder()
                .memberId(memberId)
                .orderItems(orderItems)
                .build();

        return orderRepository.save(order);
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFound(ORDER, orderId));
        findOrder.cancel();
    }

    /**
     * 주문 삭제
     */
    @Transactional
    public void deleteOrder(Long orderId) {
        Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFound(ORDER, orderId));
        orderRepository.delete(findOrder);
    }
}
