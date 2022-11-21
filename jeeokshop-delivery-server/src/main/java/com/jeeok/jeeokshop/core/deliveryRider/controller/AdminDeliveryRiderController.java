package com.jeeok.jeeokshop.core.deliveryRider.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.deliveryRider.controller.request.SaveDeliveryRiderRequest;
import com.jeeok.jeeokshop.core.deliveryRider.controller.request.UpdateDeliveryRiderRequest;
import com.jeeok.jeeokshop.core.deliveryRider.controller.response.SaveDeliveryRiderResponse;
import com.jeeok.jeeokshop.core.deliveryRider.controller.response.UpdateDeliveryRiderResponse;
import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import com.jeeok.jeeokshop.core.deliveryRider.service.DeliveryRiderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deliveryRiders")
public class AdminDeliveryRiderController {

    private final DeliveryRiderService deliveryRiderService;

    /**
     * 배송 라이더 저장
     */
    @PostMapping
    public JsonResult saveDeliveryRider(@RequestBody @Validated SaveDeliveryRiderRequest request) {

        //validate
        request.validate();

        DeliveryRider savedDeliveryRider = deliveryRiderService.saveDeliveryRider(request.toParam(), request.getRiderId());

        return JsonResult.OK(new SaveDeliveryRiderResponse(savedDeliveryRider.getId()));
    }

    /**
     * 배송 라이더 수정
     */
    @PutMapping("/{deliveryRiderId}")
    public JsonResult updateDeliveryRider(@PathVariable("deliveryRiderId") Long deliveryRiderId,
                                          @RequestBody @Validated UpdateDeliveryRiderRequest request) {

        //validate
        request.validate();

        deliveryRiderService.updateDeliveryRider(deliveryRiderId, request.toParam());
        DeliveryRider updatedDeliveryRider = deliveryRiderService.findDeliveryRider(deliveryRiderId);

        return JsonResult.OK(new UpdateDeliveryRiderResponse(updatedDeliveryRider.getId()));
    }

    /**
     * 배송 라이더 삭제
     */
    @DeleteMapping("/{deliveryRiderId")
    public JsonResult deleteDeliveryRider(@PathVariable("deliveryRiderId") Long deliveryRiderId) {

        deliveryRiderService.deleteDeliveryRider(deliveryRiderId);

        return JsonResult.OK();
    }
}
