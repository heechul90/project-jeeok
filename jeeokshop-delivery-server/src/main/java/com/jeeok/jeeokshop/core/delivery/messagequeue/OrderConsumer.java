package com.jeeok.jeeokshop.core.delivery.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.dto.KafkaSendOrderDto;
import com.jeeok.jeeokshop.core.delivery.dto.SaveDeliveryParam;
import com.jeeok.jeeokshop.core.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderConsumer {

    private final DeliveryService deliveryService;
    private final ObjectMapper objectMapper;

    @Transactional
    @KafkaListener(topics = "order")
    public void order(String kafkaMessage) throws JsonProcessingException {
        log.info("## OrderConsumer.order");
        log.info("#### kafka message = {}", kafkaMessage);

        KafkaSendOrderDto kafkaSendOrderDto = objectMapper.readValue(kafkaMessage, KafkaSendOrderDto.class);

        SaveDeliveryParam param = SaveDeliveryParam.builder()
                .memberId(kafkaSendOrderDto.getMemberId())
                .orderId(kafkaSendOrderDto.getOrderId())
                .build();
        deliveryService.saveDelivery(param);
    }

    @Transactional
    @KafkaListener(topics = "cancel")
    public void cancel(String kafkaMessage) throws JsonProcessingException {
        log.info("## OrderConsumer.cancel");
        log.info("#### kafka message = {}", kafkaMessage);

        KafkaSendOrderDto kafkaSendOrderDto = objectMapper.readValue(kafkaMessage, KafkaSendOrderDto.class);

        deliveryService.cancelByMemberIdAndOrderId(kafkaSendOrderDto.getMemberId(), kafkaSendOrderDto.getOrderId());
    }
}
