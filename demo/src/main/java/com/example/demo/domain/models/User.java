package com.example.demo.domain.models;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue Long id;
    String username;
    String password;
    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
