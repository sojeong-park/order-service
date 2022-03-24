package com.example.orderservice.controller;

import com.example.orderservice.domain.Order;
import com.example.orderservice.dto.KafkaOrderDto;
import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.messageQueue.KafkaProducer;
import com.example.orderservice.messageQueue.OrderProducer;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.OderRequest;
import com.example.orderservice.vo.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("order-service")
public class OrderController {
    private Environment env;
    private OrderService orderService;
    private KafkaProducer kafkaProducer;
    private OrderProducer orderProducer;

    public OrderController(Environment env, OrderService orderService, KafkaProducer kafkaProducer
                            , OrderProducer orderProducer) {
        this.env = env;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
        this.orderProducer = orderProducer;
    }

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's working in User service on PORT %s",
                env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/order")
    public ResponseEntity createOrder(@PathVariable(name = "userId") String userId,
                                      @RequestBody OderRequest orderRequest) {
        OrderDto orderDto = new ModelMapper().map(orderRequest, OrderDto.class);
        orderDto.setUserId(userId);

        /*OrderDto createdOrder = orderService.createOrder(orderDto);
        OrderResponse orderResponse = new ModelMapper().map(createdOrder, OrderResponse.class);

        if (orderResponse == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(orderResponse);
        }*/

        /*kafka*/
        Order order = new Order(orderDto);

        /* kafka producer message send */
        kafkaProducer.send("catalog-topic", orderDto); // 주문 수량을 catalog-service 로 전달하여 동기화
        orderProducer.send("orders", orderDto); // 주문 정보를 orders 라는 kafka topic으로 전달하여 데이터 동기화

        OrderResponse orderResponse = new ModelMapper().map(order, OrderResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @GetMapping("/{userId}/order")
    public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable(name = "userId") String userId) {
        Iterable<Order> orders = orderService.getOrdersByUserId(userId);

        List<OrderResponse> orderResponses = new ArrayList<>();
        orders.forEach(v -> {
            orderResponses.add(new ModelMapper().map(v, OrderResponse.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
    }
}
