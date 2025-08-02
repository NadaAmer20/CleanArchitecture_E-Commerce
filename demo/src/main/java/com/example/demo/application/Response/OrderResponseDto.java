package com.example.demo.application.Response;


import com.example.demo.application.dto.UserSummaryDto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private BigDecimal total;
    private String status;
    private LocalDateTime createdAt;
    private UserSummaryDto user;
    private List<OrderItemResponseDto> items;
}
