package com.jeeok.jeeokshop.core.favoritestore.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import com.jeeok.jeeokshop.core.favoritestore.dto.FavoriteStoreDto;
import com.jeeok.jeeokshop.core.favoritestore.dto.FavoriteStoreSearchCondition;
import com.jeeok.jeeokshop.core.favoritestore.service.FavoriteStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/front/favoriteStores")
public class FrontFavoriteStoreController {

    private FavoriteStoreService favoriteStoreService;

    /**
     * 내 호감 스토어 목록 조회
     */
    @GetMapping
    public JsonResult findMyFavoriteStores(@RequestHeader("member-id") Long memberId,
                                          FavoriteStoreSearchCondition condition,
                                          @PageableDefault(page = 0, size = 10) Pageable pageable) {
        condition.setSearchMemberId(memberId);

        Page<FavoriteStore> content = favoriteStoreService.findFavoriteStores(condition, pageable);
        List<FavoriteStoreDto> favoriteStores = content.stream()
                .map(FavoriteStoreDto::new)
                .collect(Collectors.toList());

        return JsonResult.OK(favoriteStores);
    }

    /**
     * 호감 스토어 단건 조회
     */
    @GetMapping("/{favoriteStoreiId}")
    public JsonResult findFavoriteStore(@PathVariable("favoriteStoreId") Long favoriteStoreId) {

        FavoriteStore findFavoriteStore = favoriteStoreService.findFavoriteStore(favoriteStoreId);
        FavoriteStoreDto favoriteStore = new FavoriteStoreDto(findFavoriteStore);

        return JsonResult.OK(favoriteStore);
    }

    @PostMapping("/{storeId}")
    public JsonResult addFavoriteStore(@RequestHeader("member-id") Long memberId,
                                       @PathVariable("storeId") Long storeId) {

        FavoriteStore savedFavoriteStore = favoriteStoreService.saveFavoriteStore(memberId, storeId);
        return JsonResult.OK();
    }
}
