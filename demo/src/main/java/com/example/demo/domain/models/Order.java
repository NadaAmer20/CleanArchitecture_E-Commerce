package com.example.demo.domain.models;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue Long id;
    @ManyToOne User user;
    BigDecimal total;
    String status;
    LocalDateTime createdAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> items;
}