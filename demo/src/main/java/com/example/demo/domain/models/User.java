package com.example.demo.domain.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id @GeneratedValue Long id;
    String username;
    String password;
    String resetCode;
    String email;
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
