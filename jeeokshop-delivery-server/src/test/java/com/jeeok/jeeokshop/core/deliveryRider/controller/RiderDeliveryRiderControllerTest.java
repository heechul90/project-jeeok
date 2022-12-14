package com.jeeok.jeeokshop.core.deliveryRider.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.entity.Address;
import com.jeeok.jeeokshop.common.entity.PhoneNumber;
import com.jeeok.jeeokshop.common.json.Code;
import com.jeeok.jeeokshop.core.IntegrationTest;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.domain.DeliveryStatus;
import com.jeeok.jeeokshop.core.deliveryRider.controller.request.EditMyDeliveryRiderRequest;
import com.jeeok.jeeokshop.core.deliveryRider.controller.request.RegisterMyDeliveryRequest;
import com.jeeok.jeeokshop.core.deliveryRider.domain.DeliveryRider;
import com.jeeok.jeeokshop.core.deliveryRider.dto.DeliveryRiderSearchCondition;
import com.jeeok.jeeokshop.core.deliveryRider.service.DeliveryRiderService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Map;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RiderDeliveryRiderControllerTest extends IntegrationTest {

    //CREATE_DELIVERY_RIDER
    public static final Long RIDER_ID_100 = 100L;
    public static final String RIDER_NAME = "?????????";
    public static final PhoneNumber PHONE_NUMBER = new PhoneNumber("010", "8922", "1189");
    public static final Address ADDRESS = new Address("88372", "?????????");
    public static final Long MEMBER_ID_1 = 1L;
    public static final Long ORDER_ID_1 = 1L;

    //UPDATE_DELIVERY_RIDER
    public static final String UPDATE_RIDER_NAME = "?????????";
    public static final PhoneNumber UPDATE_PHONE_NUMBER = new PhoneNumber("010", "8883", "1100");

    //REQUEST_INFO
    public static final String HEADER_NAME = "member-id";
    public static final String API_FIND_MY_DELIVERY_RIDERS = "/rider/deliveryRiders";
    public static final String API_FIND_MY_DELIVERY_RIDER = "/rider/deliveryRiders/{deliveryRiderId}";
    public static final String API_REGISTER_MY_DELIVERY = "/rider/deliveryRiders";
    public static final String API_EDIT_MY_DELIVERY_RIDER = "/rider/deliveryRiders/{deliveryRiderId}";
    public static final String API_CANCEL_DELIVERY_RIDER = "/rider/deliveryRiders/{deliveryRiderId}";

    @PersistenceContext protected EntityManager em;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected DeliveryRiderService deliveryRiderService;

    private DeliveryRider getDeliveryRider(Long riderId, String riderName, PhoneNumber phoneNumber, Delivery delivery) {
        return DeliveryRider.createDeliveryRider()
                .riderId(riderId)
                .riderName(riderName)
                .phoneNumber(phoneNumber)
                .delivery(delivery)
                .build();
    }

    @Test
    @DisplayName("??? ?????? ????????? ?????? ??????")
    void findMyDeliveryRiders() throws Exception {
        //given
        IntStream.range(0, 15).forEach(i -> {
            Delivery delivery = new Delivery(ADDRESS, MEMBER_ID_1 + i, ORDER_ID_1 + i);
            em.persist(delivery);
            em.persist(getDeliveryRider(RIDER_ID_100 + i, RIDER_NAME, PHONE_NUMBER, delivery));
        });

        DeliveryRiderSearchCondition condition = new DeliveryRiderSearchCondition();

        PageRequest pageRequest = PageRequest.of(0, 10);

        LinkedMultiValueMap<String, String> conditionParams = new LinkedMultiValueMap<>();
        conditionParams.setAll(objectMapper.convertValue(condition, new TypeReference<Map<String, String>>() {}));

        LinkedMultiValueMap<String, String> pageRequestParams = new LinkedMultiValueMap<>();
        pageRequestParams.add("page", String.valueOf(pageRequest.getOffset()));
        pageRequestParams.add("size", String.valueOf(pageRequest.getPageSize()));

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_MY_DELIVERY_RIDERS)
                .header(HEADER_NAME, RIDER_ID_100)
                .queryParams(conditionParams)
                .queryParams(pageRequestParams));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(1)))
                .andDo(print())
                .andDo(document("rider-findMyDeliveryRiders",
                        requestHeaders(
                                headerWithName("member-id").description("?????? ????????????")
                        ),
                        requestParameters(
                                parameterWithName("searchRiderId").ignored(),
                                parameterWithName("page").description("?????? ?????????"),
                                parameterWithName("size").description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.[*].deliveryRiderId").description("?????? ????????? ????????????"),
                                fieldWithPath("data.[*].riderId").description("????????? ????????????"),
                                fieldWithPath("data.[*].riderName").description("????????? ??????"),
                                fieldWithPath("data.[*].phoneNumber").description("???????????????"),
                                fieldWithPath("data.[*].deliveryId").description("?????? ????????????"),
                                fieldWithPath("data.[*].deliveryAddress").description("?????? ??????"),
                                fieldWithPath("data.[*].deliveryStatus").description("?????? ??????"),
                                fieldWithPath("data.[*].orderId").description("?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ?????? ????????? ??????")
    void findMyDeliveryRider() throws Exception {
        //given
        Delivery delivery = new Delivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        DeliveryRider deliveryRider = getDeliveryRider(RIDER_ID_100, RIDER_NAME, PHONE_NUMBER, delivery);
        em.persist(delivery);
        em.persist(deliveryRider);

        //when
        ResultActions resultActions = mockMvc.perform(get(API_FIND_MY_DELIVERY_RIDER, deliveryRider.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.deliveryRiderId").value(deliveryRider.getId()))
                .andExpect(jsonPath("$.data.riderId").value(RIDER_ID_100))
                .andExpect(jsonPath("$.data.riderName").value(RIDER_NAME))
                .andExpect(jsonPath("$.data.phoneNumber").value(PHONE_NUMBER.fullPhoneNumber()))
                .andExpect(jsonPath("$.data.deliveryId").value(delivery.getId()))
                .andExpect(jsonPath("$.data.deliveryAddress").value(ADDRESS.fullAddress()))
                .andExpect(jsonPath("$.data.deliveryStatus").value(DeliveryStatus.READY.getMessage()))
                .andExpect(jsonPath("$.data.orderId").value(ORDER_ID_1))
                .andDo(print())
                .andDo(document("rider-findMyDeliveryRider",
                        pathParameters(
                                parameterWithName("deliveryRiderId").description("?????? ????????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.deliveryRiderId").description("?????? ????????? ????????????"),
                                fieldWithPath("data.riderId").description("????????? ????????????"),
                                fieldWithPath("data.riderName").description("????????? ??????"),
                                fieldWithPath("data.phoneNumber").description("???????????????"),
                                fieldWithPath("data.deliveryId").description("?????? ????????????"),
                                fieldWithPath("data.deliveryAddress").description("?????? ??????"),
                                fieldWithPath("data.deliveryStatus").description("?????? ??????"),
                                fieldWithPath("data.orderId").description("?????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("????????????")
    void registerMyDelivery() throws Exception {
        //given
        Delivery delivery = new Delivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        em.persist(delivery);

        RegisterMyDeliveryRequest request = RegisterMyDeliveryRequest.builder()
                .deliveryId(delivery.getId())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post(API_REGISTER_MY_DELIVERY)
                .header(HEADER_NAME, MEMBER_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("rider-registerMyDelivery",
                        requestHeaders(
                                headerWithName("member-id").description("?????? ????????????")
                        ),
                        requestFields(
                                fieldWithPath("deliveryId").description("?????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.savedDeliveryRiderId").description("????????? ?????? ????????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("??? ?????? ????????? ??????")
    void editMyDeliveryRider() throws Exception {
        //given
        Delivery delivery = new Delivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        DeliveryRider deliveryRider = getDeliveryRider(RIDER_ID_100, RIDER_NAME, PHONE_NUMBER, delivery);
        em.persist(delivery);
        em.persist(deliveryRider);

        EditMyDeliveryRiderRequest request = EditMyDeliveryRiderRequest.builder()
                .riderName(UPDATE_RIDER_NAME)
                .phoneNumber(UPDATE_PHONE_NUMBER.fullPhoneNumber())
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put(API_EDIT_MY_DELIVERY_RIDER, deliveryRider.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andExpect(jsonPath("$.data.updatedDeliveryRiderId").value(deliveryRider.getId()))
                .andDo(print())
                .andDo(document("rider-editMyDeliveryRider",
                        pathParameters(
                                parameterWithName("deliveryRiderId").description("?????? ????????? ????????????")
                        ),
                        requestFields(
                                fieldWithPath("riderName").description("????????? ??????"),
                                fieldWithPath("phoneNumber").description("???????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data.updatedDeliveryRiderId").description("????????? ?????? ????????? ????????????")
                        )
                ));
    }

    @Test
    @DisplayName("?????? ????????? ??????")
    void cancelDeliveryRider() throws Exception {
        //given
        Delivery delivery = new Delivery(ADDRESS, MEMBER_ID_1, ORDER_ID_1);
        DeliveryRider deliveryRider = getDeliveryRider(RIDER_ID_100, RIDER_NAME, PHONE_NUMBER, delivery);
        em.persist(delivery);
        em.persist(deliveryRider);

        //when
        ResultActions resultActions = mockMvc.perform(delete(API_CANCEL_DELIVERY_RIDER, deliveryRider.getId()));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(Code.SUCCESS.name()))
                .andExpect(jsonPath("$.message").isEmpty())
                .andExpect(jsonPath("$.errors").isEmpty())
                .andDo(print())
                .andDo(document("rider-cancelDeliveryRider",
                        pathParameters(
                                parameterWithName("deliveryRiderId").description("?????? ????????? ????????????")
                        ),
                        responseFields(
                                fieldWithPath("transaction_time").description("api ?????? ??????"),
                                fieldWithPath("code").description("SUCCESS or ERROR"),
                                fieldWithPath("message").description("?????????"),
                                fieldWithPath("errors").description("??????"),
                                fieldWithPath("data").description("?????????")
                        )
                ));
    }
}