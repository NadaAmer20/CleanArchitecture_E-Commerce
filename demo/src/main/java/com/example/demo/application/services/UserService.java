package com.example.demo.application.services;

import com.example.demo.application.dto.UserDto;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import com.example.demo.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getAllUsers(String search) {
        return userRepository.findAll().stream()
                .filter(user -> isMatchingSearch(user, search))
                .map(this::mapToDto)
                .toList();
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToDto(user);
    }

    private boolean isMatchingSearch(User user, String search) {
        if (search == null || search.isEmpty()) return true;

        String lowerSearch = search.toLowerCase();
        return user.getUsername().toLowerCase().contains(lowerSearch)
                || (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerSearch));
    }

    private UserDto mapToDto(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleNames
        );
    }
}
