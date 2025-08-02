package com.example.demo.application.services;

import com.example.demo.application.Response.ApiResponse;
import com.example.demo.application.Response.OrderItemResponseDto;
import com.example.demo.application.Response.OrderResponseDto;
import com.example.demo.application.dto.CreateOrderDto;
import com.example.demo.application.dto.UserSummaryDto;

import com.example.demo.domain.models.Order;
import com.example.demo.domain.models.OrderItem;
import com.example.demo.domain.models.Product;
import com.example.demo.domain.models.User;
import com.example.demo.domain.repositories.OrderRepository;
import com.example.demo.domain.repositories.ProductRepository;
import com.example.demo.domain.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    @Transactional
    public ApiResponse createOrder(CreateOrderDto dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CreateOrderDto.OrderItemDto itemDto : dto.getItems()) {
            Product product = productRepo.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.getQuantity());

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemDto.getQuantity());
            item.setPriceAtOrder(BigDecimal.valueOf(product.getPrice()));
            items.add(item);

            total = total.add(item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        Order order = new Order();
        order.setUser(user);
        order.setTotal(total);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        items.forEach(i -> i.setOrder(order));
        order.setItems(items);

        orderRepo.save(order);
        return ApiResponse.ok("Order created", mapToDto(order));
    }

    public ApiResponse getUserOrders(Long userId, int page, int size) {
        Page<Order> orders = orderRepo.findByUserId(userId, PageRequest.of(page, size));

        List<OrderResponseDto> dtos = orders.getContent().stream()
                .map(this::mapToDto)
                .toList();

        return ApiResponse.ok("User orders fetched", dtos);
    }

    private OrderResponseDto mapToDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        User user = order.getUser();
        com.example.demo.application.dto.UserSummaryDto userDto = new com.example.demo.application.dto.UserSummaryDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        dto.setUser(userDto);

        List<OrderItemResponseDto> items = order.getItems().stream().map(item -> {
            OrderItemResponseDto itemDto = new OrderItemResponseDto();
            itemDto.setId(item.getId());
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPriceAtOrder(item.getPriceAtOrder().doubleValue());
            return itemDto;
        }).toList();

        dto.setItems(items);

        return dto;
    }

}
