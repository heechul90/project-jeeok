package com.jeeok.jeeokshop.core.delivery.domain;

import com.jeeok.jeeokshop.core.delivery.service.DeliverySender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.persistence.PostUpdate;

@Slf4j
public class DeliveryListener {

    @Autowired
    @Lazy
    private DeliverySender deliverySender;

    /**
     * UPDATE 이후 호출
     */
    @PostUpdate
    public void postUpdate(Delivery delivery) {
        log.info("[DeliveryListener] {}", delivery.getStatus().getMessage());

        if (DeliveryStatus.DELIVERY.equals(delivery.getStatus())) {
            try {
                deliverySender.delivery(delivery);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage());
            }
        } else if (DeliveryStatus.COMPLETE.equals(delivery.getStatus())) {
            try {
                deliverySender.complete(delivery);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage());
            }
        } else if (DeliveryStatus.CANCEL.equals(delivery.getStatus())) {
            try {
                deliverySender.cancel(delivery);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage());
            }
        }
    }
}
