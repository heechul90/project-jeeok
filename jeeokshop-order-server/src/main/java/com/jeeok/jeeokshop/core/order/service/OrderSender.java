package com.jeeok.jeeokshop.core.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeeok.jeeokshop.core.order.domain.Order;
import com.jeeok.jeeokshop.core.order.dto.KafkaSendOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderSender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void order(Order order) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        KafkaSendOrderDto kafkaSendOrderDto = KafkaSendOrderDto.createPrimitiveField(order);
        String jsonInString = objectMapper.writeValueAsString(kafkaSendOrderDto);
        kafkaTemplate.send("order", jsonInString);
        log.info("Kafka producer sent data from order microservice = {}", kafkaSendOrderDto.toString());
    }

    public void cancel(Order order) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        KafkaSendOrderDto kafkaSendOrderDto = KafkaSendOrderDto.createPrimitiveField(order);
        String jsonInString = objectMapper.writeValueAsString(kafkaSendOrderDto);
        kafkaTemplate.send("cancel", jsonInString);
        log.info("Kafka producer sent data from order microservice = {}", kafkaSendOrderDto.toString());
    }
}
