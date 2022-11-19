package com.jeeok.jeeokshop.core.item.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.item.controller.request.EditItemRequest;
import com.jeeok.jeeokshop.core.item.controller.request.RegisterItemRequest;
import com.jeeok.jeeokshop.core.item.controller.response.SaveItemResponse;
import com.jeeok.jeeokshop.core.item.controller.response.UpdateItemResponse;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.jeeok.jeeokshop.core.item.dto.ManagerItemDto;
import com.jeeok.jeeokshop.core.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/manager/stores/{storeId}/items")
public class ManagerItemController {

    private final ItemService itemService;

    /**
     * 내 스토어 상품 목록
     */
    @GetMapping
    public JsonResult findItems(@PathVariable("storeId") Long storeId, ItemSearchCondition condition) {
        condition.setSearchStoreId(storeId);
        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

        Page<Item> content = itemService.findItems(condition, pageRequest);
        List<ManagerItemDto> items = content.stream()
                .map(ManagerItemDto::new)
                .collect(Collectors.toList());
        return JsonResult.OK(items);
    }

    /**
     * 내 스토어 상품
     */
    @GetMapping("/{itemId}")
    public JsonResult findItem(@PathVariable("storeId") Long storeId, @PathVariable("itemId") Long itemId) {
        Item findItem = itemService.findItem(itemId);
        ManagerItemDto item = new ManagerItemDto(findItem);
        return JsonResult.OK(item);
    }

    /**
     * 내 스토어 상품 등록
     */
    @PostMapping
    public JsonResult registerItem(@PathVariable("storeId") Long storeId,
                                   @RequestBody @Validated RegisterItemRequest request) {

        //validate
        request.validate();

        Item savedItem = itemService.saveItem(request.toParam(), storeId, request.getCategoryId());
        return JsonResult.OK(new SaveItemResponse(savedItem.getId()));

    }

    /**
     * 내 스토어 상품 수정
     */
    @PutMapping("/{itemId}")
    public JsonResult editItem(@PathVariable("storeId") Long storeId, @PathVariable("itemId") Long itemId,
                               @RequestBody @Validated EditItemRequest request) {

        //validate
        request.validate();

        itemService.updateItem(itemId, request.toParam());
        Item updatedItem = itemService.findItem(itemId);

        return JsonResult.OK(new UpdateItemResponse(updatedItem.getId()));
    }

    /**
     * 내 스토어 상품 삭제
     */
    @DeleteMapping("/{itemId}")
    public JsonResult deleteItem(@PathVariable("storeId") Long storeId, @PathVariable("itemId") Long itemId) {
        itemService.deleteItem(itemId);
        return JsonResult.OK();
    }
}
