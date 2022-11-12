package com.jeeok.jeeokshop.core.store.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.store.controller.request.EditStoreRequest;
import com.jeeok.jeeokshop.core.store.controller.request.ResisterStoreRequest;
import com.jeeok.jeeokshop.core.store.controller.response.EditStoreResponse;
import com.jeeok.jeeokshop.core.store.controller.response.ResisterStoreResponse;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.StoreDto;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import com.jeeok.jeeokshop.core.store.service.StoreService;
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
@RequestMapping("/front/stores")
public class FrontStoreController {

    private final StoreService storeService;

    /**
     * 내 스토어 목록 조회
     */
    @GetMapping
    public JsonResult findMyStores(@RequestHeader(value = "member-id") Long memberId,
                                           StoreSearchCondition condition,
                                           @PageableDefault(page = 0, size = 10) Pageable pageable) {
        condition.setSearchMemberId(memberId);

        Page<Store> content = storeService.findStores(condition, pageable);
        List<StoreDto> stores = content.stream()
                .map(StoreDto::new)
                .collect(Collectors.toList());

        return JsonResult.OK(stores);
    }

    /**
     * 내 스토어 단건 조회
     */
    @GetMapping("/{storeId}")
    public JsonResult findMyStore(@PathVariable("storeId") Long storeId) {
        Store findStore = storeService.findStore(storeId);
        StoreDto store = new StoreDto(findStore);
        return JsonResult.OK(store);
    }

    /**
     * 내 스토어 등록
     */
    @PostMapping
    public JsonResult registerStore(@RequestHeader(value = "member-id") Long memberId, @RequestBody @Validated ResisterStoreRequest request) {

        //validate
        request.validate();

        Store registeredStore = storeService.saveStore(request.toParam(memberId));

        return JsonResult.OK(new ResisterStoreResponse(registeredStore.getId()));
    }

    /**
     * 내 스토어 수정
     */
    @PutMapping("/{storeId}")
    public JsonResult editStore(@PathVariable("storeId") Long storeId,
                                @RequestBody @Validated EditStoreRequest request) {

        //validate
        request.validate();

        storeService.updateStore(storeId, request.toParam());
        Store findStore = storeService.findStore(storeId);

        return JsonResult.OK(new EditStoreResponse(findStore.getId()));
    }

    /**
     * 내 스토어 삭제
     */
    @DeleteMapping("/{storeId}")
    public JsonResult deleteStore(@PathVariable("storeId") Long storeId) {

        storeService.deleteStore(storeId);

        return JsonResult.OK();
    }
}
