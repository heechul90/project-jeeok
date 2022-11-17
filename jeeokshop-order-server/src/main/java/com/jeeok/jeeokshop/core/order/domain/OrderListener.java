package com.jeeok.jeeokshop.core.order.domain;

import com.jeeok.jeeokshop.core.order.service.OrderSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostUpdate;

@Slf4j
public class OrderListener {

    @Autowired
    @Lazy
    private OrderSender orderSender;

    @PostUpdate
    public void postUpdate(Order order) {
        OrderStatus status = order.getStatus();
        log.info("[OrderListener] {}", OrderStatus.ORDER.name());

        if (status.equals(OrderStatus.ORDER)) {
            try {
                orderSender.order(order);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage());
            }
        } else if (status.equals(OrderStatus.CANCEL)) {
            try {
                orderSender.cancel(order);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
    }
}
