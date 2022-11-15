package com.jeeok.jeeokshop.core.store.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.store.controller.request.SaveStoreRequest;
import com.jeeok.jeeokshop.core.store.controller.request.UpdateStoreRequest;
import com.jeeok.jeeokshop.core.store.controller.response.SaveStoreResponse;
import com.jeeok.jeeokshop.core.store.controller.response.UpdateStoreResponse;
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
@RequestMapping("/admin/stores")
public class AdminStoreController {

    private final StoreService storeService;

    /**
     * 스토어 목록 조회
     */
    @GetMapping
    public JsonResult findStores(@RequestHeader("member-id") Long memberId,
                                 StoreSearchCondition condition,
                                 @PageableDefault(page = 0, size = 10) Pageable pageable) {
        //내 목록 가져오기 위해
        condition.setSearchMemberId(memberId);

        Page<Store> content = storeService.findStores(condition, pageable);
        List<StoreDto> stores = content.stream()
                .map(StoreDto::new)
                .collect(Collectors.toList());
        return JsonResult.OK(stores);
    }

    /**
     * 스토어 단건 조회
     */
    @GetMapping("/{storeId}")
    public JsonResult findStore(@PathVariable("storeId") Long storeId) {
        Store findStore = storeService.findStore(storeId);
        StoreDto store = new StoreDto(findStore);
        return JsonResult.OK(store);
    }

    /**
     * 스토어 저장
     */
    @PostMapping
    public JsonResult saveStore(@RequestHeader("member-id") Long memberId, @RequestBody @Validated SaveStoreRequest request) {

        //validate
        request.validate();

        Store savedStore = storeService.saveStore(request.toParam(memberId));

        return JsonResult.OK(new SaveStoreResponse(savedStore.getId()));
    }

    /**
     * 스토어 수정
     */
    @PutMapping("/{storeId}")
    public JsonResult updateStore(@PathVariable("storeId") Long storeId, @RequestBody @Validated UpdateStoreRequest request) {

        //validate
        request.validate();

        storeService.updateStore(storeId, request.toParam());
        Store updatedStore = storeService.findStore(storeId);

        return JsonResult.OK(new UpdateStoreResponse(updatedStore.getId()));
    }

    /**
     * 스토어 삭제
     */
    @DeleteMapping("/{storeId}")
    public JsonResult deleteStore(@PathVariable("storeId") Long storeId) {

        storeService.deleteStore(storeId);

        return JsonResult.OK();
    }
}
