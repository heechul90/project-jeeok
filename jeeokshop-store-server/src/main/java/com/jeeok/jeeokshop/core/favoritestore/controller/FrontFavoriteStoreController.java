package com.jeeok.jeeokshop.core.favoritestore.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import com.jeeok.jeeokshop.core.favoritestore.dto.FavoriteStoreSearchCondition;
import com.jeeok.jeeokshop.core.favoritestore.service.FavoriteStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/front/favoriteStores")
public class FrontFavoriteStoreController {

    private FavoriteStoreService favoriteStoreService;

    /**
     * 내 호감 스토어 목록
     */
    @GetMapping
    public JsonResult findMyFavoriteStore(@RequestHeader("member-id") Long memberId,
                                          FavoriteStoreSearchCondition condition,
                                          @PageableDefault(page = 0, size = 10) Pageable pageable) {

        condition.setSearchMemberId(memberId);

        Page<FavoriteStore> content = favoriteStoreService.findFavoriteStores(condition, pageable);

        return JsonResult.OK();
    }
}
