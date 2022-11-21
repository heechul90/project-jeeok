package com.jeeok.jeeokshop.core.delivery.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.delivery.controller.response.RiderFindDeliveryResponse;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.delivery.dto.DeliverySearchCondition;
import com.jeeok.jeeokshop.core.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/deliveries")
public class RiderDeliveryController {

    private final DeliveryService deliveryService;

    /**
     * 배송 목록
     */
    @GetMapping
    public JsonResult findDeliveries(DeliverySearchCondition condition, @PageableDefault(page = 0, size = 10) Pageable pageable) {

        //기본값으로 배송준비
        condition.setSearchDeliveryStatus(condition.getSearchDeliveryStatus() != null ? DeliveryStatus.READY : null);

        Page<Delivery> content = deliveryService.findDeliveries(condition, pageable);
        List<RiderFindDeliveryResponse> deliveries = content.stream()
                .map(RiderFindDeliveryResponse::new)
                .collect(Collectors.toList());
        return JsonResult.OK(deliveries);
    }

    /**
     * 배송 상세
     */
    @GetMapping("/{deliveryId}")
    public JsonResult findDelivery(@PathVariable("deliveryId") Long deliveryId) {
        Delivery findDelivery = deliveryService.findDelivery(deliveryId);
        RiderFindDeliveryResponse delivery = new RiderFindDeliveryResponse(findDelivery);
        return JsonResult.OK(delivery);
    }
}
