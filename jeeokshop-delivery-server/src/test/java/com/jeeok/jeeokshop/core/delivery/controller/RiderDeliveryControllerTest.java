package com.jeeok.jeeokshop.core.delivery.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.delivery.dto.DeliverySearchCondition;
import com.jeeok.jeeokshop.core.delivery.service.DeliveryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

class RiderDeliveryControllerTest extends IntegrationTest {

    //CREATE_DELIVERY
    public static final Long DELIVERY_ID_1 = 1L;
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;
    public static final Address ADDRESS = new Address("38273", "?????????");

    //REQUEST_INFO
    public static final String API_FIND_DELIVERIES = "/rider/deliveries";
    public static final String API_FIND_DELIVERY = "/rider/deliveries/{deliveryId}";

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
    @DisplayName("?????? ??????")
    void findDeliveries() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> em.persist(getDelivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1)));

        DeliverySearchCondition condition = new DeliverySearchCondition();

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {
        }));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_DELIVERIES).
                queryParams(conditionParams)
                .queryParams(pageRequestParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(10)))
                .andDo(print())
                .andDo(document("rider-findDeliveries",
                        requestParameters(
                                parameterWithName("searchDeliveryStatus").ignored(),
                                parameterWithName("page").description("?????? ?????????"),
                                parameterWithName("size").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data[*].deliveryId").description("?????? ????????????"),
                                fieldWithPath("data[*].address").description("?????? ??????"),
                                fieldWithPath("data[*].deliveryStatus").description("?????? ??????"),
                                fieldWithPath("data[*].memberId").description("?????? ????????????"),
                                fieldWithPath("data[*].orderId").description("?????? ????????????"),
                                fieldWithPath("data[*].riderId").description("????????? ????????????"),
                                fieldWithPath("data[*].riderName").description("????????? ??????"),
                                fieldWithPath("data[*].phoneNumber").description("???????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ??????")
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
                .andDo(document("rider-findDelivery",
                        pathParameters(
                                parameterWithName("deliveryId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.deliveryId").description("?????? ????????????"),
                                fieldWithPath("data.address").description("?????? ??????"),
                                fieldWithPath("data.deliveryStatus").description("?????? ??????"),
                                fieldWithPath("data.memberId").description("?????? ????????????"),
                                fieldWithPath("data.orderId").description("?????? ????????????"),
                                fieldWithPath("data.riderId").description("????????? ????????????"),
                                fieldWithPath("data.riderName").description("????????? ??????"),
                                fieldWithPath("data.phoneNumber").description("???????????????")
                        )
                ));
    }
}