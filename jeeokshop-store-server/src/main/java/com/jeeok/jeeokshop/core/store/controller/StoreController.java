package com.jeeok.jeeokshop.core.store.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.StoreDto;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import com.jeeok.jeeokshop.core.store.service.StoreService;
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
@RequestMapping("/stores")
public class StoreController {

    private final StoreService storeService;

    /**
     * 스토어 목록
     */
    @GetMapping
    public JsonResult findStores(StoreSearchCondition condition, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Store> content = storeService.findStores(condition, pageable);
        List<StoreDto> stores = content.stream()
                .map(StoreDto::new)
                .collect(Collectors.toList());
        return JsonResult.OK(stores);
    }

    /**
     * 스토어 상세
     */
    @GetMapping("/{storeId}")
    public JsonResult findStore(@PathVariable("storeId") Long storeId) {
        Store findStore = storeService.findStore(storeId);
        StoreDto store = new StoreDto(findStore);
        return JsonResult.OK(store);
    }
}
