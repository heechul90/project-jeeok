package com.jeeok.jeeokshop.core.item.service;

import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemDto;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.jeeok.jeeokshop.core.item.repository.ItemQueryRepository;
import com.jeeok.jeeokshop.core.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemQueryRepository itemQueryRepository;
    private final ItemRepository itemRepository;

    /**
     * 상품 목록 조회
     */
    public List<ItemDto> findItems(ItemSearchCondition condition, Pageable pageable) {
        Page<Item> content = itemQueryRepository.findItems(condition, pageable);
        return content.stream()
                .map(ItemDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 상품 단거 조회
     */

    /**
     * 상품 저장
     */

    /**
     * 상품 수정
     */

    /**
     * 상품 삭제
     */


}
