package com.example.demo.domain.repositories;
import com.example.demo.application.dto.RoleType;
import com.example.demo.domain.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}