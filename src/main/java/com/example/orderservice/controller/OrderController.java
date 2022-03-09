package com.example.orderservice.controller;

import com.example.orderservice.domain.Order;
import com.example.orderservice.dto.OrderDto;
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

    public OrderController(Environment env, OrderService orderService) {
        this.env = env;
        this.orderService = orderService;
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

        OrderDto createdOrder = orderService.createOrder(orderDto);
        OrderResponse orderResponse = new ModelMapper().map(createdOrder, OrderResponse.class);

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
