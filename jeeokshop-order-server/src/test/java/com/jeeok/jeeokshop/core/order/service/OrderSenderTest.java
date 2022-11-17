package com.jeeok.jeeokshop.core.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeeok.jeeokshop.core.order.dto.KafkaSendOrderDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@EnableKafka
@DirtiesContext
@EmbeddedKafka(topics = {"order"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderSenderTest {

    private final String TEST_TOPIC = "order";

    private Consumer<Integer, String> consumer;
    private KafkaTemplate<String, String> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @BeforeEach
    void beforeEach() {
        System.out.println("beforeAll");
        producer = configureProducer();
        consumer = configureConsumer();
    }

    private Consumer<Integer, String> configureConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<Integer, String> consumer = new DefaultKafkaConsumerFactory<Integer, String>(consumerProps)
                .createConsumer();
        consumer.subscribe(Collections.singleton(TEST_TOPIC));
        return consumer;
    }

    private KafkaTemplate<String, String> configureProducer() {
        HashMap<String, Object> producerProps = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerProps));
    }

    @Test
    @DisplayName("order시 kafka message 전송 테스트")
    void sendOrder() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        KafkaSendOrderDto kafkaSendOrderDto = KafkaSendOrderDto.builder()
                .orderId(1L)
                .memberId(1L)
                .itemIds(List.of(1L, 2L))
                .build();
        String jsonInString = objectMapper.writeValueAsString(kafkaSendOrderDto);

        //when
        ListenableFuture<SendResult<String, String>> order = producer.send("order", jsonInString);
        System.out.println("order.get().toString() = " + order.get().toString());

        //then
        ConsumerRecord<Integer, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TEST_TOPIC);
        assertThat(singleRecord).isNotNull();
        assertThat(singleRecord.value()).isEqualTo(jsonInString);
    }
}