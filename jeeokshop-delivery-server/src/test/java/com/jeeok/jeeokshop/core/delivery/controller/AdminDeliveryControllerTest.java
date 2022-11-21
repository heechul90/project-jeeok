package com.jeeok.jeeokshop.core.delivery.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.delivery.controller.request.SaveDeliveryRequest;
import com.jeeok.jeeokshop.core.delivery.controller.request.UpdateDeliveryRequest;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.delivery.dto.DeliverySearchCondition;
import com.jeeok.jeeokshop.core.delivery.service.DeliveryService;
import org.hamcrest.Matchers;
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

class AdminDeliveryControllerTest extends IntegrationTest {

    //CREATE_DELIVERY
    public static final Long DELIVERY_ID_1 = 1L;
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final Address ADDRESS = new Address("38273", "서울시");

    //UPDATE_DELIVERY
    public static final Address UPDATE_ADDRESS = new Address("33871", "세종시");

    //REQUEST_INFO
    public static final String API_FIND_DELIVERIES = "/admin/deliveries";
    public static final String API_FIND_DELIVERY = "/admin/deliveries/{deliveryId}";
    public static final String API_SAVE_DELIVERY = "/admin/deliveries";
    public static final String API_UPDATE_DELIVERY = "/admin/deliveries/{deliveryId}";
    public static final String API_DELIVERY = "/admin/deliveries/{deliveryId}/delivery";
    public static final String API_COMPLETE = "/admin/deliveries/{deliveryId}/complete";
    public static final String API_CANCEL = "/admin/deliveries/{deliveryId}/cancel";
    public static final String API_DELETE_DELIVERY = "/admin/deliveries/{deliveryId}";

    @PersistenceContext protected EntityManager em;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected DeliveryService deliveryService;

    private Delivery getDelivery(Address address, Long memberId, Long orderId) {
        return Delivery.createDelivery()
                .address(address)
                .memberId(memberId)
                .orderId(orderId)
                .build();
    }

    @Test
    @DisplayName("배송 목록 조회")
    void findDeliveries() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1)));

        DeliverySearchCondition condition = new DeliverySearchCondition();
        condition.setSearchDeliveryStatus(DeliveryStatus.READY);

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_DELIVERIES)
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
                .andDo(document("admin-findDeliveries",
                        requestParameters(
                                parameterWithName("searchDeliveryStatus").description("배송 상태"),
                                parameterWithName("page").description("검색 페이지"),
                                parameterWithName("size").description("검색 사이즈")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data[*].deliveryId").description("배송 고유번호"),
                                fieldWithPath("data[*].address").description("배송 주소"),
                                fieldWithPath("data[*].deliveryStatus").description("배송 상태"),
                                fieldWithPath("data[*].memberId").description("멤버 고유번호"),
                                fieldWithPath("data[*].orderId").description("주문 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("배송 단건 조회")
    void findDelivery() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_DELIVERY, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.deliveryId").value(delivery.getId()))
                .andExpect(jsonPath("$.data.address").value(ADDRESS.fullAddress()))
                .andExpect(jsonPath("$.data.deliveryStatus").value(DeliveryStatus.READY.getMessage()))
                .andExpect(jsonPath("$.data.memberId").value(MEMBER_ID_1))
                .andExpect(jsonPath("$.data.orderId").value(ORDER_ID_1))
                .andDo(print())
                .andDo(document("admin-findDelivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("배송 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.deliveryId").description("배송 고유번호"),
                                fieldWithPath("data.address").description("배송 주소"),
                                fieldWithPath("data.deliveryStatus").description("배송 상태"),
                                fieldWithPath("data.memberId").description("멤버 고유번호"),
                                fieldWithPath("data.orderId").description("주문 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("배송 저장")
    void saveDelivery() throws Exception {
        //given
        SaveDeliveryRequest request = SaveDeliveryRequest.builder()
                .memberId(MEMBER_ID_1)
                .orderId(ORDER_ID_1)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_SAVE_DELIVERY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-saveDelivery",
                        requestFields(
                                fieldWithPath("memberId").description("멤버 고유번호"),
                                fieldWithPath("orderId").description("주문 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.savedDeliveryId").description("저장된 배송 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("배송 수정")
    void updateDelivery() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        UpdateDeliveryRequest request = UpdateDeliveryRequest.builder()
                .zipcode(UPDATE_ADDRESS.getZipcode())
                .address(UPDATE_ADDRESS.getAddress())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_UPDATE_DELIVERY, delivery.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedDeliveryId").value(delivery.getId()))
                .andDo(print())
                .andDo(document("admin-updateDelivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("배송 고유번호")
                        ),
                        requestFields(
                                fieldWithPath("zipcode").description("우편번호"),
                                fieldWithPath("address").description("주소")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data.updatedDeliveryId").description("수정된 배송 고유번호")
                        )
                ));
    }

    @Test
    @DisplayName("배송 시작")
    void delivery() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(put(API_DELIVERY, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-delivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("배송 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }

    @Test
    @DisplayName("배송 완료")
    void complete() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(put(API_COMPLETE, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-complete",
                        pathParameters(
                                parameterWithName("deliveryId").description("배송 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }

    @Test
    @DisplayName("배송 취소")
    void cancel() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(put(API_CANCEL, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-cancel",
                        pathParameters(
                                parameterWithName("deliveryId").description("배송 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }

    @Test
    @DisplayName("배송 삭제")
    void deleteDelivery() throws Exception {
        //given
        Delivery delivery = getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_DELETE_DELIVERY, delivery.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("admin-deleteDelivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("배송 고유번호")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api 요청 시간"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("메시지"),
                                fieldWithPath("errors").description("에러"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }
}