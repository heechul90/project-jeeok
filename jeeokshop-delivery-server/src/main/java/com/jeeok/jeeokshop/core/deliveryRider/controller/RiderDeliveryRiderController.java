package com.jeeok.jeeokshop.core.deliveryRider.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.deliveryRider.controller.request.EditMyDeliveryRiderRequest;
import com.jeeok.jeeokshop.core.deliveryRider.controller.request.RegisterMyDeliveryRequest;
import com.jeeok.jeeokshop.core.deliveryRider.controller.response.EditMyDeliveryRiderResponse;
import com.jeeok.jeeokshop.core.deliveryRider.controller.response.RegisterMyDeliveryResponse;
import com.jeeok.jeeokshop.core.deliveryRider.controller.response.FindMyDeliveryRiderResponse;
import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import com.jeeok.jeeokshop.core.deliveryRider.dto.DeliveryRiderSearchCondition;
import com.jeeok.jeeokshop.core.deliveryRider.service.DeliveryRiderService;
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
@RequestMapping("/rider/deliveryRiders")
public class RiderDeliveryRiderController {

    private final DeliveryRiderService deliveryRiderService;

    /**
     * 내 배송 라이더 목록 조회
     */
    @GetMapping
    public JsonResult findMyDeliveryRiders(@RequestHeader("member-id") Long riderId,
                                           DeliveryRiderSearchCondition condition,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable) {
        //내 배송 라이더 목록 가져오기 위해 setting
        condition.setSearchRiderId(riderId);

        Page<DeliveryRider> content = deliveryRiderService.findDeliveryRiders(condition, pageable);
        List<FindMyDeliveryRiderResponse> deliveryRiders = content.stream()
                .map(FindMyDeliveryRiderResponse::new)
                .collect(Collectors.toList());
        return JsonResult.OK(deliveryRiders);
    }

    /**
     * 내 배송 라이더 상세
     */
    @GetMapping("/{deliveryRiderId}")
    public JsonResult findMyDeliveryRider(@PathVariable("deliveryRiderId") Long deliveryRiderId) {
        DeliveryRider findDeiveryRider = deliveryRiderService.findDeliveryRider(deliveryRiderId);
        FindMyDeliveryRiderResponse deliveryRider = new FindMyDeliveryRiderResponse(findDeiveryRider);
        return JsonResult.OK(deliveryRider);
    }

    /**
     * 배송하기
     */
    @PostMapping
    public JsonResult RegisterMyDelivery(@RequestHeader("member-id") Long riderId,
                               @RequestBody @Validated RegisterMyDeliveryRequest request) {

        //validate
        request.validate();

        DeliveryRider savedDeliveryRider = deliveryRiderService.saveDeliveryRider(request.toParam(), riderId);

        return JsonResult.OK(new RegisterMyDeliveryResponse(savedDeliveryRider.getId()));
    }

    /**
     * 내 배송 라이더 수정
     */
    @PutMapping("/{deliveryRiderId}")
    public JsonResult EditMyDeliveryRider(@PathVariable("deliveryRiderId") Long deliveryRiderId,
                                          @RequestBody @Validated EditMyDeliveryRiderRequest request) {

        //validate
        request.validate();

        deliveryRiderService.updateDeliveryRider(deliveryRiderId, request.toParam());
        DeliveryRider updatedDeliveryRider = deliveryRiderService.findDeliveryRider(deliveryRiderId);

        return JsonResult.OK(new EditMyDeliveryRiderResponse(updatedDeliveryRider.getId()));
    }

    /**
     * 배송 라이더 취소
     */
    @DeleteMapping("/{deliveryRiderId}")
    public JsonResult cancelDeliveryRider(@PathVariable("deliveryRiderId") Long deliveryRiderId) {
        deliveryRiderService.deleteDeliveryRider(deliveryRiderId);
        return JsonResult.OK();
    }
}
