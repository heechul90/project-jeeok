package com.jeeok.jeeokshop.core.store.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import com.jeeok.jeeokshop.core.store.service.StoreService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FrontStoreControllerTest extends IntegrationTest {

    //CREATE_STORE
    public static final String CATEGORY_NAME = "category";
    public static final int ORDER = 1;
    public static final String STORE_NAME = "store_name";
    public static final BusinessHours BUSINESS_HOURS = new BusinessHours("1700", "2200");
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1234", "5678");
    public static final Address ADDRESS = new Address("12345", "서울시");
    public static final Long MEMBER_ID = 1L;

    //REQUEST_INFO
    public static final String API_FIND_STORES = "/front/stores";
    public static final String FRONT_FIND_STORE = "/front/stores/{storeId}";

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper objectMapper;

    @PersistenceContext protected EntityManager em;

    @Autowired protected StoreService storeService;

    private Store getStore(String name, BusinessHours businessHours, PhoneNumber phoneNumber, Address address, Long memberId, List<Category> categories) {
        return Store.createStore()
                .name(name)
                .businessHours(businessHours)
                .phoneNumber(phoneNumber)
                .address(address)
                .memberId(memberId)
                .categories(categories)
                .build();
    }

    private Category getCategory(String category, Integer order) {
        return Category.createCategory()
                .name(category)
                .order(order)
                .build();
    }

    @Nested
    class SuccessfulTest {

        @Test
        @DisplayName("스토어 목록")
        @Rollback(value = false)
        void findStores() throws Exception {
            //given
            List<Category> categories = new ArrayList<>();
            IntStream.range(0, 3).forEach(i -> categories.add(getCategory(CATEGORY_NAME + i, ORDER + i)));

            IntStream.range(0, 20).forEach(i -> em.persist(getStore(STORE_NAME + i, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID, categories)));

            StoreSearchCondition condition = new StoreSearchCondition();
            condition.setSearchCondition(SearchCondition.NAME);
            condition.setSearchKeyword(STORE_NAME);

            PageRequest pageRequest = PageRequest.of(0, 10);

            LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
            conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

            LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
            pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
            pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

            //when
            ResultActions resultActions = mockMvc.perform(get(API_FIND_STORES)
                    .queryParams(conditionParams)
                    .queryParams(pageRequestParams));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                    .andExpect(jsonPath("$.message").isEmpty())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.data.length()", Matchers.is(10)))
                    .andDo(print())
                    .andDo(document("front-findStores",
                            requestParameters(
                                    parameterWithName("searchCondition").description("검색 조건"),
                                    parameterWithName("searchKeyword").description("검색 키워드"),
                                    parameterWithName("searchMemberId").ignored(),
                                    parameterWithName("page").description("검색 페이지"),
                                    parameterWithName("size").description("검색 사이즈")
                            ),
                            responseFields(
                                    fieldWithPath("transaction_time").description("api 요청 시간"),
                                    fieldWithPath("code").description("SUCCESS or ERROR"),
                                    fieldWithPath("message").description("메시지"),
                                    fieldWithPath("errors").description("에러"),
                                    fieldWithPath("data[*].storeId").description("스토어 고유번호"),
                                    fieldWithPath("data[*].storeName").description("스토어 이름"),
                                    fieldWithPath("data[*].businessOpeningHours").description("스토어 영업 시작 시간"),
                                    fieldWithPath("data[*].businessClosingHours").description("스토어 영업 종료 시간"),
                                    fieldWithPath("data[*].phoneNumber").description("스토어 전화번호"),
                                    fieldWithPath("data[*].address").description("스토어 주소"),
                                    fieldWithPath("data[*].storeCategories[*].categoryName").description("카테고리 이름"),
                                    fieldWithPath("data[*].storeCategories[*].categoryOrder").description("카테고리 정렬번호")
                            )
                    ));
        }

        @Test
        @DisplayName("스토어 상세")
        void findStore() throws Exception {
            //given
            List<Category> categories = new ArrayList<>();
            IntStream.range(0, 3).forEach(i -> categories.add(getCategory(CATEGORY_NAME + i, ORDER + i)));

            Store store = getStore(STORE_NAME, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID, categories);
            em.persist(store);

            //when
            ResultActions resultActions = mockMvc.perform(get(FRONT_FIND_STORE, store.getId()));

            //then
            resultActions
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                    .andExpect(jsonPath("$.message").isEmpty())
                    .andExpect(jsonPath("$.errors").isEmpty())
                    .andExpect(jsonPath("$.data.storeName").value(STORE_NAME))
                    .andExpect(jsonPath("$.data.businessOpeningHours").value(BUSINESS_HOURS.getOpeningHours()))
                    .andExpect(jsonPath("$.data.businessClosingHours").value(BUSINESS_HOURS.getClosingHours()))
                    .andExpect(jsonPath("$.data.phoneNumber").value(PHONE_NUMBER.fullPhoneNumber()))
                    .andExpect(jsonPath("$.data.address").value(ADDRESS.fullAddress()))
                    .andExpect(jsonPath("$.data.storeCategories.length()", Matchers.is(3)))
                    .andDo(print())
                    .andDo(document("front-findStore",
                            pathParameters(
                                    parameterWithName("storeId").description("스토어 고유 번호")
                            ),
                            responseFields(
                                    fieldWithPath("transaction_time").description("api 요청 시간"),
                                    fieldWithPath("code").description("SUCCESS or ERROR"),
                                    fieldWithPath("message").description("메시지"),
                                    fieldWithPath("errors").description("에러"),
                                    fieldWithPath("data.storeId").description("스토어 고유번호"),
                                    fieldWithPath("data.storeName").description("스토어 이름"),
                                    fieldWithPath("data.businessOpeningHours").description("스토어 영업 시작 시간"),
                                    fieldWithPath("data.businessClosingHours").description("스토어 영업 종료 시간"),
                                    fieldWithPath("data.phoneNumber").description("스토어 전화번호"),
                                    fieldWithPath("data.address").description("스토어 주소"),
                                    fieldWithPath("data.storeCategories[*].categoryName").description("카테고리 이름"),
                                    fieldWithPath("data.storeCategories[*].categoryOrder").description("카테고리 정렬번호")
                            )
                    ));
        }
    }
}
