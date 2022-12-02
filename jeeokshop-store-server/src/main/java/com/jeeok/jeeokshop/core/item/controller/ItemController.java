package com.jeeok.jeeokshop.core.item.controller;

import com.jeeok.jeeokshop.common.json.JsonResult;
import com.jeeok.jeeokshop.core.item.controller.response.FindItemResponse;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public JsonResult findItem(@PathVariable("itemId") Long itemId) {
        Item findItem = itemService.findItem(itemId);
        FindItemResponse item = new FindItemResponse(findItem);
        return JsonResult.OK(item);
    }
}
