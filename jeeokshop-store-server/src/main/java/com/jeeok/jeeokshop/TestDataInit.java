package com.jeeok.jeeokshop;

import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.favoritestore.domain.FavoriteStore;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final InitService initService;

    @PostConstruct
    public void init() {

        //교촌치킨
        initService.kyochonInit();

        //BHC치킨
        initService.bhcInit();


    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        @PersistenceContext protected EntityManager em;

        private Category getCategory(String name, int order) {
            return Category.createCategory()
                    .name(name)
                    .order(order)
                    .build();
        }

        private Store getStore(String name, BusinessHours businessHours, Address address, long memberId, List<Category> categories) {
            return Store.createStore()
                    .name(name)
                    .businessHours(businessHours)
                    .address(address)
                    .memberId(memberId)
                    .categories(categories)
                    .build();
        }

        private Item getItem(String name, int price, int stockQuantity, Store store, Category category) {
            return Item.createItem()
                    .name(name)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .photo(null)
                    .store(store)
                    .category(category)
                    .build();
        }

        public void kyochonInit() {
            List<Category> categories = new ArrayList<>();
            Category kyochonSeries = getCategory("교촌시리즈", 1);
            Category honeySeries = getCategory("허니시리즈", 2);
            categories.add(kyochonSeries);
            categories.add(honeySeries);

            Store kyochon = getStore("교촌치킨", new BusinessHours("1700", "2200"), new Address("34291", "서울시 강남구"), 3L, categories);
            em.persist(kyochon);

            Item kombo = getItem("교촌콤보", 19000, 100, kyochon, kyochonSeries);

            Item original = getItem("교촌오리지날", 16000, 150, kyochon, kyochonSeries);
            em.persist(kombo);
            em.persist(original);

            em.persist(FavoriteStore.createFavoriteStore()
                    .memberId(1L)
                    .store(kyochon)
                    .build());
            em.persist(FavoriteStore.createFavoriteStore()
                    .memberId(2L)
                    .store(kyochon)
                    .build());
        }

        public void bhcInit() {
            List<Category> categories = new ArrayList<>();
            Category category1 = getCategory("치퐁당후라이드", 1);
            Category category2 = getCategory("포테킹후라이드", 2);
            Category category3 = getCategory("싸이순살", 3);
            categories.add(category1);
            categories.add(category2);

            Store bhc = getStore("BHC치킨", new BusinessHours("1800", "2400"), new Address("12864", "서울시 강북구"), 4L, categories);
            em.persist(bhc);

            Item item1 = getItem("치퐁당후라이드", 20000, 200, bhc, category1);
            Item item2 = getItem("히바네로후라이드", 20000, 100, bhc, category2);
            Item item3 = getItem("포테킹후라이드", 19000, 100, bhc, category2);
            Item item4 = getItem("싸이순살", 20900, 50, bhc, category3);

            em.persist(item1);
            em.persist(item2);
            em.persist(item3);
            em.persist(item4);

            em.persist(FavoriteStore.createFavoriteStore()
                    .memberId(1L)
                    .store(bhc)
                    .build());
            em.persist(FavoriteStore.createFavoriteStore()
                    .memberId(2L)
                    .store(bhc)
                    .build());
        }
    }
}
