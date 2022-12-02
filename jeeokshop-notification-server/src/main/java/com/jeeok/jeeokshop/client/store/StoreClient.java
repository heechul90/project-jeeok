package com.jeeok.jeeokshop.client.store;

import com.jeeok.jeeokshop.common.json.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("JEEOKSHOP-STORE-SERVER")
public interface StoreClient {

    @GetMapping("/items/{itemId}")
    JsonResult<FindItemResponse> findItem(@PathVariable("itemId") Long itemId);

}
