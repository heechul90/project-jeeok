package com.jeeok.jeeokshop.core.order.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.order.controller.request.OrderRequest;
import com.jeeok.jeeokshop.core.order.controller.response.OrderResponse;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.dto.OrderDto;
import com.jeeok.jeeokshop.core.order.dto.OrderSearchCondition;
import com.jeeok.jeeokshop.core.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/front/orders")
public class FrontOrderController {

    private final OrderService orderService;

    /**
     * 내 주문 목록
     */
    @GetMapping
    public JsonResult findMyOrders(@RequestHeader("member-id") Long memberId,
                                 OrderSearchCondition condition,
                                 @PageableDefault(page = 0, size = 10) Pageable pageable) {
        condition.setSearchMemberId(memberId);
        Page<Order> content = orderService.findOrders(condition, pageable);
        List<OrderDto> orders = content.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
        return JsonResult.OK(orders);
    }

    /**
     * 내 주문 상세
     */
    @GetMapping("/{orderId}")
    public JsonResult findMyOrder(@PathVariable("orderId") Long orderId) {
        Order findOrder = orderService.findOrder(orderId);
        OrderDto order = new OrderDto(findOrder);
        return JsonResult.OK(order);
    }

    /**
     * 주문
     */
    public JsonResult order(@RequestHeader("member-id") Long memberId, @RequestBody @Validated OrderRequest request) {

        //validate
        request.validate();

        Order order = orderService.saveOrder(memberId, request.toParam());

        return JsonResult.OK(new OrderResponse(order.getId()));
    }

    /**
     * 주문 취소
     */
    @PutMapping("/{orderId}/cancel")
    public JsonResult cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return JsonResult.OK();
    }

    /**
     * 주문 삭제
     */
    @DeleteMapping("/{orderId}")
    public JsonResult deleteOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(orderId);
        return JsonResult.OK();
    }
}
