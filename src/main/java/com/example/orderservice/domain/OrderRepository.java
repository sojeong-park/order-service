package com.example.orderservice.domain;

import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
    Order findByOrderId(String orderId);
    Iterable<Order> findByUserId(String userId);
}
