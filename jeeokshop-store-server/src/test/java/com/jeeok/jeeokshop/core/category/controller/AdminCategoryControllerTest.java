package com.jeeok.jeeokshop.core.category.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.dto.SearchCondition;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.category.controller.request.SaveCategoryRequest;
import com.jeeok.jeeokshop.core.category.controller.request.UpdateCategoryRequest;
import com.jeeok.jeeokshop.core.category.domain.Category;
import com.jeeok.jeeokshop.core.category.dto.CategorySearchCondition;
import com.jeeok.jeeokshop.core.category.service.CategoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminCategoryControllerTest extends IntegrationTest {

    //CREATE_CATEGORY
    public static final Long CATEGORY_ID_1 = 1L;
    public static final String NAME = "A시리즈";
    public static final int ORDER = 1;
    public static final long STORE_ID_1 = 1L;

    //UPDATE_CATEGORY
    public static final String UPDATE_NAME = "B시지르";
    public static final int UPDATE_ORDER = 2;

    //REQUEST_INFO
    public static final String API_FIND_CATEGORIES = "/admin/categories";
    public static final String API_FIND_CATEGORY = "/admin/categories/{categoryId}";
    public static final String API_SAVE_CATEGORY = "/admin/categories";
    public static final String API_UPDATE_CATEGORY = "/admin/categories/{categoryId}";
    public static final String API_DELETE_CATEGORY = "/admin/categories/{categoryId}";

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @PersistenceContext protected EntityManager em;
    @Autowired protected CategoryService categoryService;

    private Category getCategory(String name, int order) {
        return Category.createCategory()
                .name(name)
                .order(order)
                .build();
    }

    Category category;

    @BeforeEach
    void beforeEach() {
        category = getCategory(NAME, ORDER);
    }

    @Test
    @DisplayName("카테고리 목록 조회")
    void findCategories() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getCategory(NAME + i, (ORDER + i))));

        CategorySearchCondition condition = new CategorySearchCondition();
        condition.setSearchCondition(SearchCondition.NAME);
        condition.setSearchKeyword(NAME);

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_CATEGORIES)
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
                .andDo(document("admin-findCategories",
                        requestParameters(
                                parameterWithName("searchMemberId").ignored(),
                                parameterWithName("searchStoreId").ignored(),
                                parameterWithName("searchCondition").description("검색 조건"),
                                parameterWithName("searchKeyword").description("검색 키워드"),
                                parameterWithName("page").description("검색 페이지"),
                                parameterWithName("size").description("검색 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data[*].categoryId").description("카테고리 고유번호"),
                                fieldWithPath("data[*].categoryName").description("카테고리명"),
                                fieldWithPath("data[*].categoryOrder").description("정렬번호")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 단건 조회")
    void findCategory() throws Exception {
        //given
        em.persist(category);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_CATEGORY, category.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.categoryId").value(category.getId()))
                .andExpect(jsonPath("$.data.categoryName").value(NAME))
                .andExpect(jsonPath("$.data.order").value(ORDER))
                .andDo(print())
                .andDo(document("admin-findCategory",
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.categoryId").description("카테고리 고유번호"),
                                fieldWithPath("data.categoryName").description("카테고리명"),
                                fieldWithPath("data.categoryOrder").description("정렬번호")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 저장")
    void saveCategory() throws Exception {
        //given
        SaveCategoryRequest request = SaveCategoryRequest.builder()
                .categoryName(NAME)
                .categoryOrder(ORDER)
                .storeId(STORE_ID_1)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_SAVE_CATEGORY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-saveCategory",
                        requestFields(
                                fieldWithPath("categoryName").description("카테고리명"),
                                fieldWithPath("categoryOrder").description("정렬번호"),
                                fieldWithPath("storeId").description("스토어 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.savedCategoryId").description("저장된 카테고리 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategory() throws Exception {
        //given
        em.persist(category);

        UpdateCategoryRequest request = UpdateCategoryRequest.builder()
                .categoryName(UPDATE_NAME)
                .categoryOrder(UPDATE_ORDER)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_UPDATE_CATEGORY, category.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-updateCategory",
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("categoryName").description("카테고리명"),
                                fieldWithPath("categoryOrder").description("정렬번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.updatedCategoryId").description("수정된 상품 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("카테고리 삭제")
    void deleteCategory() throws Exception {
        //given
        em.persist(category);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_CATEGORY, category.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-deleteCategory",
                        pathParameters(
                                parameterWithName("categoryId").description("카테고리 고유번호")
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