package com.example.orderservice.vo;

import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;

@Data
public class OrderResponse implements Serializable {
    private String productId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;

    private String userId;
    private String orderId;
}
