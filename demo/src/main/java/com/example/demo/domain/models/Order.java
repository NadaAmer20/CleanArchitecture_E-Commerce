package com.example.demo.domain.models;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id @GeneratedValue Long id;
    @ManyToOne User user;
    BigDecimal total;
    String status;
    LocalDateTime createdAt;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> items;
}