package com.jeeok.jeeokshop.core.delivery.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.delivery.controller.request.SaveDeliveryRequest;
import com.jeeok.jeeokshop.core.delivery.controller.request.UpdateDeliveryRequest;
import com.jeeok.jeeokshop.core.delivery.controller.response.SaveDeliveryResponse;
import com.jeeok.jeeokshop.core.delivery.controller.response.UpdateDeliveryResponse;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.dto.DeliveryDto;
import com.jeeok.jeeokshop.core.delivery.dto.DeliverySearchCondition;
import com.jeeok.jeeokshop.core.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deliveries")
public class AdminDeliveryController {

    private final DeliveryService deliveryService;

    /**
     * 배송 목록 조회
     */
    @GetMapping
    public JsonResult findDeliveries(DeliverySearchCondition condition, Pageable pageable) {
        Page<Delivery> content = deliveryService.findDeliveries(condition, pageable);
        List<DeliveryDto> deliveries = content.stream()
                .map(DeliveryDto::new)
                .collect(Collectors.toList());
        return JsonResult.OK(deliveries);
    }

    /**
     * 배송 단건 조회
     */
    @GetMapping("/{deliveryId}")
    public JsonResult findDelivery(@PathVariable("deliveryId") Long deliveryId) {
        Delivery findDelivery = deliveryService.findDelivery(deliveryId);
        DeliveryDto delivery = new DeliveryDto(findDelivery);
        return JsonResult.OK(delivery);
    }

    /**
     * 배송 저장
     */
    @PostMapping
    public JsonResult saveDelivery(@RequestBody @Validated SaveDeliveryRequest request) {

        //validate
        request.validate();

        Delivery savedDelivery = deliveryService.saveDelivery(request.toDelivery());
        return JsonResult.OK(new SaveDeliveryResponse(savedDelivery.getId()));
    }

    /**
     * 배송 수정
     */
    @PutMapping("/{deliveryId}")
    public JsonResult updateDelivery(@PathVariable("deliveryId") Long deliveryId,
                                     @RequestBody @Validated UpdateDeliveryRequest request) {

        //validate
        request.validate();

        deliveryService.updateDelivery(deliveryId, request.toParam());
        Delivery updatedDelivery = deliveryService.findDelivery(deliveryId);

        return JsonResult.OK(new UpdateDeliveryResponse(updatedDelivery.getId()));
    }

    /**
     * 배송 시작
     */
    @PutMapping("/{deliveryId}/delivery")
    public JsonResult delivery(@PathVariable("deliveryId") Long deliveryId) {
        deliveryService.delivery(deliveryId);
        return JsonResult.OK();
    }

    /**
     * 배송 완료
     */
    @PutMapping("/{deliveryId}/complete")
    public JsonResult complete(@PathVariable("deliveryId") Long deliveryId) {
        deliveryService.complete(deliveryId);
        return JsonResult.OK();
    }

    /**
     * 배송 취소
     */
    @PutMapping("/{deliveryId}/cancel")
    public JsonResult cancel(@PathVariable("deliveryId") Long deliveryId) {
        deliveryService.cancelByDeliveryId(deliveryId);
        return JsonResult.OK();
    }

    /**
     * 배송 삭제
     */
    @DeleteMapping("/{deliveryId}")
    public JsonResult deleteDelivery(@PathVariable("deliveryId") Long deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return JsonResult.OK();
    }
}
