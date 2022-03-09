package com.example.orderservice.vo;

import lombok.Data;

@Data
public class OderRequest {
    String productId;
    String quantity;
    String unitPrice;
}
