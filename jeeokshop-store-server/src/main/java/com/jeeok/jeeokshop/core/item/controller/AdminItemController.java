package com.jeeok.jeeokshop.core.item.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.item.controller.request.SaveItemRequest;
import com.jeeok.jeeokshop.core.item.controller.request.UpdateItemRequest;
import com.jeeok.jeeokshop.core.item.controller.response.SaveItemResponse;
import com.jeeok.jeeokshop.core.item.controller.response.UpdateItemResponse;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemDto;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.jeeok.jeeokshop.core.item.service.ItemService;
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
@RequestMapping("/admin/items")
public class AdminItemController {

    private final ItemService itemService;

    /**
     * 상품 목록 조회
     */
    @GetMapping
    public JsonResult findItems(ItemSearchCondition condition, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<Item> content = itemService.findItems(condition, pageable);
        List<ItemDto> items = content.stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());
        return JsonResult.OK(items);
    }

    /**
     * 상품 단건 조회
     */
    @GetMapping("/{itemId}")
    public JsonResult findItem(@PathVariable("itemId") Long itemId) {
        Item findItem = itemService.findItem(itemId);
        ItemDto item = new ItemDto(findItem);
        return JsonResult.OK(item);
    }

    /**
     * 상품 저장
     */
    @PostMapping
    public JsonResult saveItem(@RequestBody @Validated SaveItemRequest request) {

        //validate
        request.validate();

        Long savedItemId = itemService.saveItem(request.toParam(), request.getStoreId(), request.getCategoryId());

        return JsonResult.OK(new SaveItemResponse(savedItemId));
    }

    /**
     * 상품 수정
     */
    @PutMapping("/{itemId}")
    public JsonResult updateItem(@PathVariable("itemId") Long itemId, @RequestBody @Validated UpdateItemRequest request) {

        //validate
        request.validate();

        itemService.updateItem(itemId, request.toParam());
        Item updatedItem = itemService.findItem(itemId);

        return JsonResult.OK(new UpdateItemResponse(updatedItem.getId()));
    }

    /**
     * 상품 삭제
     */
    @DeleteMapping("/{itemId}")
    public JsonResult deleteItem(@PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return JsonResult.OK();
    }
}
