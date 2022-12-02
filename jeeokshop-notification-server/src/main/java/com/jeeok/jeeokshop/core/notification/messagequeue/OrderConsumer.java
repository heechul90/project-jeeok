package com.jeeok.jeeokshop.core.notification.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeeok.jeeokshop.common.entity.DeliveryStatus;
import com.jeeok.jeeokshop.core.notification.dto.KafkaSendOrderDto;
import com.jeeok.jeeokshop.core.notification.dto.SaveNotificationParam;
import com.jeeok.jeeokshop.core.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderConsumer {

    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;

    @Transactional
    @KafkaListener(topics = "order")
    public void order(String kafkaMessage) throws JsonProcessingException {
        log.info("## OrderConsumer.order");
        log.info("#### kafka message = {}", kafkaMessage);

        KafkaSendOrderDto kafkaSendOrderDto = objectMapper.readValue(kafkaMessage, KafkaSendOrderDto.class);

        IntStream.range(0, kafkaSendOrderDto.getItemIds().size()).forEach(i -> {
            notificationService.saveNotification(
                    SaveNotificationParam.builder()
                            .memberId(kafkaSendOrderDto.getMemberId())
                            .itemId(kafkaSendOrderDto.getItemIds().get(i))
                            .title(DeliveryStatus.READY.getMessage())
                            .message("배송준비중입니다.")
                            .build()
            );
        });
    }
}
