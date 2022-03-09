package com.example.orderservice.domain;

import com.example.orderservice.dto.OrderDto;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=120, nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer totalPrice;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false, updatable = false, insertable = false)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public String getProductId() {
        return productId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Order() {}

    public Order(OrderDto orderDto){
        this.orderId = UUID.randomUUID().toString();
        this.quantity = orderDto.getQuantity();
        this.unitPrice = orderDto.getUnitPrice();
        this.userId = orderDto.getUserId();
        this.totalPrice = this.quantity * this.unitPrice;
        this.productId = orderDto.getProductId();
    }
}
