package com.jeeok.jeeokshop.core.item.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.Photo;
import com.jeeok.jeeokshop.common.entity.Yn;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.item.controller.request.EditItemRequest;
import com.jeeok.jeeokshop.core.item.controller.request.RegisterItemRequest;
import com.jeeok.jeeokshop.core.item.domain.Item;
import com.jeeok.jeeokshop.core.item.dto.ItemSearchCondition;
import com.jeeok.jeeokshop.core.item.service.ItemService;
import com.jeeok.jeeokshop.core.store.domain.BusinessHours;
import com.jeeok.jeeokshop.core.store.domain.PhoneNumber;
import com.jeeok.jeeokshop.core.store.domain.Store;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ManagerItemControllerTest extends IntegrationTest {

    //CREATE_ITEM
    public static final String ITEM_NAME = "교촌오리지날";
    public static final int PRICE = 10000;
    public static final int STOCK_QUANTITY = 100;
    public static final Photo PHOTO = new Photo("https://jeeok.com/item/picture", "교촌오리지날");
    public static final Yn SALES_YN = Yn.Y;
    public static final Long MEMBER_ID_1 = 1L;

    //REQUEST_INFO
    public static final String API_FIND_ITEMS = "/manager/stores/{storeId}/items";
    public static final String API_FIND_ITEM = "/manager/stores/{storeId}/items/{itemId}";
    public static final String API_REGISTER_ITEM = "/manager/stores/{storeId}/items";
    public static final String API_EDIT_ITEM = "/manager/stores/{storeId}/items/{itemId}";
    public static final String API_DELETE_ITEM = "/manager/stores/{storeId}/items/{itemId}";

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @PersistenceContext protected EntityManager em;
    @Autowired protected ItemService itemService;

    Category category;
    Store store;

    @BeforeEach
    void beforeEach() {
        category = Category.createCategory()
                .name("시리즈 카테고리")
                .order(1)
                .build();

        store = Store.createStore()
                .name("교촌치킨")
                .businessHours(new BusinessHours("1700", "2200"))
                .phoneNumber(new PhoneNumber("010", "1111", "2222"))
                .address(new Address("11234", "서울시"))
                .memberId(MEMBER_ID_1)
                .categories(List.of(category))
                .build();

        em.persist(store);
    }

    private Item getItem(String name, int price, int stockQuantity, Photo photo) {
        Item item = Item.createItem()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .photo(photo)
                .store(store)
                .category(category)
                .build();
        return item;
    }

    @Test
    @DisplayName("내 스토어 상품 목록")
    void findItems() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getItem(ITEM_NAME + i, PRICE, STOCK_QUANTITY, PHOTO)));

        ItemSearchCondition condition = new ItemSearchCondition();
        condition.setSearchCondition(SearchCondition.NAME);
        condition.setSearchKeyword(ITEM_NAME);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_ITEMS, store.getId())
                .queryParams(conditionParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(15)))
                .andDo(print())
                .andDo(MockMvcRestDocumentation.document("manager-findItems",
                        pathParameters(
                                parameterWithName("storeId").description("스토어 고유번호")
                        ),
                        requestParameters(
                                parameterWithName("searchStoreId").ignored(),
                                parameterWithName("searchCondition").description("검색 조건"),
                                parameterWithName("searchKeyword").description("검색 키워드")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data[*].itemId").description("상품 고유번호"),
                                fieldWithPath("data[*].itemName").description("상품 이름"),
                                fieldWithPath("data[*].salesYn").description("판매여부"),
                                fieldWithPath("data[*].price").description("상품 가격"),
                                fieldWithPath("data[*].stockQuantity").description("재고수량"),
                                fieldWithPath("data[*].photo").description("상품 이미지")
                        )
                ));
    }

    @Test
    @DisplayName("내 스토어 상품 상세")
    void findItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_ITEM, store.getId(), item.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.itemId").value(item.getId()))
                .andExpect(jsonPath("$.data.itemName").value(ITEM_NAME))
                .andExpect(jsonPath("$.data.salesYn").value(SALES_YN.name()))
                .andExpect(jsonPath("$.data.price").value(PRICE))
                .andExpect(jsonPath("$.data.photo").value(PHOTO.fullPhotoPath()))
                .andDo(print())
                .andDo(document("manager-findItem",
                        pathParameters(
                                parameterWithName("storeId").description("스토어 고유번호"),
                                parameterWithName("itemId").description("상품 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.itemId").description("상품 고유번호"),
                                fieldWithPath("data.itemName").description("상품 이름"),
                                fieldWithPath("data.salesYn").description("판매여부"),
                                fieldWithPath("data.price").description("상품 가격"),
                                fieldWithPath("data.stockQuantity").description("재고수량"),
                                fieldWithPath("data.photo").description("상품 이미지")
                        )
                ));
    }

    @Test
    @DisplayName("상품 등록")
    void registerItem() throws Exception {
        //given
        RegisterItemRequest request = RegisterItemRequest.builder()
                .itemName(ITEM_NAME)
                .itemPrice(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .photoPath(PHOTO.getPath())
                .photoName(PHOTO.getName())
                .categoryId(category.getId())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_REGISTER_ITEM, store.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("manager-registerItem",
                        pathParameters(
                                parameterWithName("storeId").description("스토어 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("itemName").description("상품 이름"),
                                fieldWithPath("itemPrice").description("상품 가격"),
                                fieldWithPath("stockQuantity").description("재고수량"),
                                fieldWithPath("photoPath").description("이미지 주소"),
                                fieldWithPath("photoName").description("이미지 이름"),
                                fieldWithPath("categoryId").description("카테고리 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.savedItemId").description("저장된 상품 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("내 스토어 수정")
    void editItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        EditItemRequest request = EditItemRequest.builder()
                .itemName(ITEM_NAME)
                .salesYn(SALES_YN)
                .itemPrice(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .photoPath(PHOTO.getPath())
                .photoName(PHOTO.getName())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_EDIT_ITEM, store.getId(), item.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedItemId").value(item.getId()))
                .andDo(print())
                .andDo(document("manager-editItem",
                        pathParameters(
                                parameterWithName("storeId").description("스토어 고유번호"),
                                parameterWithName("itemId").description("상품 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("itemName").description("상품 이름"),
                                fieldWithPath("salesYn").description("판매 여부"),
                                fieldWithPath("itemPrice").description("상품 가격"),
                                fieldWithPath("stockQuantity").description("재고수량"),
                                fieldWithPath("photoPath").description("이미지 주소"),
                                fieldWithPath("photoName").description("이미지 이름")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.updatedItemId").description("수정된 상품 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("내 스토어 상품 삭제")
    void deleteItem() throws Exception {
        //given
        Item item = getItem(ITEM_NAME, PRICE, STOCK_QUANTITY, PHOTO);
        em.persist(item);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_ITEM, store.getId(), item.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("manager-deleteItem",
                        pathParameters(
                                parameterWithName("storeId").description("스토어 고유번호"),
                                parameterWithName("itemId").description("상품 고유번호")
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