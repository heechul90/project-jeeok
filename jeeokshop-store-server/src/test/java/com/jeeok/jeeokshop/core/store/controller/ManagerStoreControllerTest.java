package com.jeeok.jeeokshop.core.store.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.store.controller.request.EditStoreRequest;
import com.jeeok.jeeokshop.core.store.controller.request.ResisterStoreRequest;
import com.jeeok.jeeokshop.core.store.controller.request.SaveStoreRequest;
import com.jeeok.jeeokshop.core.store.controller.request.UpdateStoreRequest;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import com.jeeok.jeeokshop.core.store.dto.SaveStoreParam;
import com.jeeok.jeeokshop.core.store.dto.StoreSearchCondition;
import com.jeeok.jeeokshop.core.store.service.StoreService;
import com.netflix.discovery.converters.Auto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ManagerStoreControllerTest extends IntegrationTest {

    //CREATE_STORE
    public static final String CATEGORY_NAME = "시리즈A";
    public static final int ORDER = 1;
    public static final String STORE_NAME = "교촌치킨";
    public static final BusinessHours BUSINESS_HOURS = new BusinessHours("1700", "2200");
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "1234", "5678");
    public static final Address ADDRESS = new Address("12345", "서울시");
    public static final Long MEMBER_ID = 1L;

    //UPDATE_STORE
    public static final String UPDATE_STORE_NAME = "BHC치킨";
    public static final BusinessHours UPDATE_BUSINESS_HOURS = new BusinessHours("1800", "2300");
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "8765", "4321");
    public static final Address UPDATE_ADDRESS = new Address("54321", "세종시");

    //ERROR_MESSAGE

    //REQUEST_INFO
    public static final String HEADER_NAME = "member-id";
    public static final String API_FIND_STORES = "/manager/stores";
    public static final String API_FIND_STORE = "/manager/stores/{storeId}";
    public static final String API_SAVE_STORE = "/manager/stores";
    public static final String API_UPDATE_STORE = "/manager/stores/{storeId}";
    public static final String API_DELETE_STORE = "/manager/stores/{storeId}";

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

    Store store = null;

    @BeforeEach
    void beforeEach() {
        List<Category> categories = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> categories.add(getCategory(CATEGORY_NAME + i, ORDER + i)));
        store = getStore(STORE_NAME, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID, categories);
    }

    @Test
    @DisplayName("내 스토어 목록")
    void findStores() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getStore(STORE_NAME + i, BUSINESS_HOURS, PHONE_NUMBER, ADDRESS, MEMBER_ID,
                List.of(getCategory(CATEGORY_NAME + i, ORDER), getCategory(CATEGORY_NAME + (i + 1), (ORDER + 1)))
        )));

        StoreSearchCondition condition = new StoreSearchCondition();
        condition.setSearchCondition(SearchCondition.NAME);
        condition.setSearchKeyword(STORE_NAME);

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //expected
        mockMvc.perform(get(API_FIND_STORES)
                        .header(HEADER_NAME, MEMBER_ID)
                        .queryParams(conditionParams)
                        .queryParams(pageRequestParams))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(10)))
                .andDo(print())
                .andDo(document("manager-findStores",
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
    @DisplayName("내 스토어 상세")
    void findStore() throws Exception {
        //given
        em.persist(store);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_STORE, store.getId()));

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
                .andDo(document("manager-findStore",
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

    @Test
    @DisplayName("스토어 등록")
    void registerStore() throws Exception {
        //given
        List<ResisterStoreRequest.StoreCategoryRequest> storeCategoryRequests = new ArrayList<>();
        IntStream.range(0, 3).forEach(i -> storeCategoryRequests.add(
                ResisterStoreRequest.StoreCategoryRequest.builder()
                        .categoryName(CATEGORY_NAME + i)
                        .categoryOrder(ORDER + i)
                        .build()
        ));

        ResisterStoreRequest request = ResisterStoreRequest.builder()
                .storeName(STORE_NAME)
                .businessOpeningHours(BUSINESS_HOURS.getOpeningHours())
                .businessClosingHours(BUSINESS_HOURS.getClosingHours())
                .phoneNumber(PHONE_NUMBER.fullPhoneNumber())
                .zipcode(ADDRESS.getZipcode())
                .address(ADDRESS.getAddress())
                .storeCategories(storeCategoryRequests)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_SAVE_STORE)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HEADER_NAME, MEMBER_ID.toString())
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("manager-registerStore",
                        requestHeaders(
                                headerWithName("member-id").description("회원 고유번호(입력하지 말것!)")
                        ),
                        requestFields(
                                fieldWithPath("storeName").description("스토어 이름"),
                                fieldWithPath("businessOpeningHours").description("스토어 영업 시작 시간"),
                                fieldWithPath("businessClosingHours").description("스토어 영업 종료 시간"),
                                fieldWithPath("phoneNumber").description("스토어 전화번호"),
                                fieldWithPath("zipcode").description("스토어 우편번호"),
                                fieldWithPath("address").description("스토어 주소"),
                                fieldWithPath("storeCategories[*].categoryName").description("카테고리 이름"),
                                fieldWithPath("storeCategories[*].categoryOrder").description("카테고리 정렬번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.savedStoreId").description("저장된 스토어 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("스토어 수정")
    void editStore() throws Exception{
        //given
        store.getCategories().add(Category.createCategory().name("new" + CATEGORY_NAME).order(ORDER + 3).build());
        em.persist(store);

        List<EditStoreRequest.UpdateStoreCategory> storeCategories = store.getCategories().stream()
                .map(category -> EditStoreRequest.UpdateStoreCategory.builder()
                        .categoryId(category.getId())
                        .categoryName(category.getName())
                        .categoryOrder(category.getOrder())
                        .build()
                ).collect(Collectors.toList());

        EditStoreRequest request = EditStoreRequest.builder()
                .storeName(UPDATE_STORE_NAME)
                .businessOpeningHours(UPDATE_BUSINESS_HOURS.getOpeningHours())
                .businessClosingHours(UPDATE_BUSINESS_HOURS.getClosingHours())
                .phoneNumber(PHONE_NUMBER.fullPhoneNumber())
                .zipcode(ADDRESS.getZipcode())
                .address(ADDRESS.getAddress())
                .storeCategories(storeCategories)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_UPDATE_STORE, store.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("manager-editStore",
                        pathParameters(
                                parameterWithName("storeId").description("스토어 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("storeName").description("스토어 이름"),
                                fieldWithPath("businessOpeningHours").description("스토어 영업 시작 시간"),
                                fieldWithPath("businessClosingHours").description("스토어 영업 종료 시간"),
                                fieldWithPath("phoneNumber").description("스토어 전화번호"),
                                fieldWithPath("zipcode").description("스토어 우편번호"),
                                fieldWithPath("address").description("스토어 주소"),
                                fieldWithPath("storeCategories[*].categoryId").description("카테고리 고유번호"),
                                fieldWithPath("storeCategories[*].categoryName").description("카테고리 이름"),
                                fieldWithPath("storeCategories[*].categoryOrder").description("카테고리 정렬번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.updatedStoreId").description("수정된 스토어 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("내 스토어 삭제")
    void deleteStore() throws Exception {
        //given
        em.persist(store);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_STORE, store.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("manager-deleteStore",
                        pathParameters(
                                parameterWithName("storeId").description("스토어 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data").description("데이터")
                        )
                ));
    }
}