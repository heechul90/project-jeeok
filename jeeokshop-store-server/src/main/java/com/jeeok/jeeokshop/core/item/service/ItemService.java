package com.jeeok.jeeokshop.core.item.service;

import com.jeeok.jeeokshop.common.exception.EntityNotFound;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.repository.CategoryRepository;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.jeeok.jeeokshop.core.item.dto.SaveItemParam;
import com.jeeok.jeeokshop.core.item.dto.UpdateItemParam;
import com.jeeok.jeeokshop.core.item.repository.ItemQueryRepository;
import com.jeeok.jeeokshop.core.item.repository.ItemRepository;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    public static final String ITEM = "Item";
    public static final String STORE = "Store";
    public static final String CATEGORY = "Category";
    private final ItemQueryRepository itemQueryRepository;
    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 상품 목록 조회
     */
    public Page<Item> findItems(ItemSearchCondition condition, Pageable pageable) {
        return itemQueryRepository.findItems(condition, pageable);
    }

    /**
     * 상품 단거 조회
     */
    public Item findItem(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFound(ITEM, itemId.toString()));
    }

    /**
     * 상품 저장
     */
    @Transactional
    public Item saveItem(SaveItemParam param, Long storeId, Long categoryId) {
        Store findStore = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFound(STORE, storeId.toString()));

        /*findStore.getCategories().stream()
                .filter(category -> category.getId() == categoryId)
                .findAny()
                .orElseThrow(() -> new EntityNotFound(CATEGORY, categoryId.toString()));*/

        Category findCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFound(CATEGORY, categoryId.toString()));

        Item item = Item.createItem()
                .name(param.getName())
                .price(param.getPrice())
                .photo(param.getPhoto())
                .store(findStore)
                .category(findCategory)
                .build();

        return itemRepository.save(item);
    }

    /**
     * 상품 수정
     */
    @Transactional
    public void updateItem(Long itemId, UpdateItemParam param) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFound(ITEM, itemId.toString()));
        findItem.updateItem(param);
    }

    /**
     * 상품 삭제
     */
    @Transactional
    public void deleteItem(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFound(ITEM, itemId.toString()));
        itemRepository.delete(findItem);
    }
}
