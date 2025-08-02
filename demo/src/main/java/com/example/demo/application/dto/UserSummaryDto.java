package com.example.demo.application.dto;


import lombok.Data;

@Data
public class UserSummaryDto {
    private Long id;
    private String username;
    private String email;
}