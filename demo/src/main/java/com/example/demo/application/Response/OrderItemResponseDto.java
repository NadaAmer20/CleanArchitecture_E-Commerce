package com.example.demo.application.Response;


import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double priceAtOrder;
}