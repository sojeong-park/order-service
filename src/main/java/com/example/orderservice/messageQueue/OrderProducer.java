package com.example.orderservice.messageQueue;

import com.example.orderservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OrderProducer {
    private KafkaTemplate<String, String> kafkaTemplate; // kafkaTemplate이용해 메시지 Send

    List<Field> fieldList = Arrays.asList(new Field("String", true, "order_id"),
            new Field("String", true, "user_id"),
            new Field("String", true, "product_id"),
            new Field("int32", true, "quantity"),
            new Field("int32", true, "unit_price"),
            new Field("int32", true, "total_price"));

    Schema schema = Schema.builder().type("Struct")
            .fieldList(fieldList)
            .optional(false)
            .name("orders")
            .build();

    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderDto send(String topic, OrderDto orderDto) {
        Payload payload = Payload.builder()
                .order_id(orderDto.getOrderId())
                .product_id(orderDto.getProductId())
                .user_id(orderDto.getUserId())
                .quantity(orderDto.getQuantity())
                .unit_price(orderDto.getUnitPrice())
                .unit_price(orderDto.getTotalPrice())
                .build();

        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = objectMapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer send message" + kafkaOrderDto);

        return orderDto;
    }
}
