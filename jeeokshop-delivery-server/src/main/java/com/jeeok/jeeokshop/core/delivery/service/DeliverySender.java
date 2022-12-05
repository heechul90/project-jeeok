package com.jeeok.jeeokshop.core.delivery.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeeok.jeeokshop.core.delivery.domain.Delivery;
import com.jeeok.jeeokshop.core.delivery.dto.KafkaSendDeliveryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliverySender {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void delivery(Delivery delivery) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        KafkaSendDeliveryDto kafkaSendDeliveryDto = KafkaSendDeliveryDto.createPrimitiveField(delivery);
        String jsonInString = objectMapper.writeValueAsString(kafkaSendDeliveryDto);
        kafkaTemplate.send("delivery", jsonInString);
        log.info("Kafka producer sent data from delivery microservice = {}", kafkaSendDeliveryDto.toString());
    }

    public void complete(Delivery delivery) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        KafkaSendDeliveryDto kafkaSendDeliveryDto = KafkaSendDeliveryDto.createPrimitiveField(delivery);
        String jsonInString = objectMapper.writeValueAsString(kafkaSendDeliveryDto);
        kafkaTemplate.send("complete", jsonInString);
        log.info("kafka producer sent data from delivery microservice = {}", kafkaSendDeliveryDto.toString());
    }

    public void cancel(Delivery delivery) throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        KafkaSendDeliveryDto kafkaSendDeliveryDto = KafkaSendDeliveryDto.createPrimitiveField(delivery);
        String jsonInString = objectMapper.writeValueAsString(kafkaSendDeliveryDto);
        kafkaTemplate.send("cancel", jsonInString);
        log.info("kafka producer sent data from delivery microservice = {}", kafkaSendDeliveryDto.toString());
    }
}
