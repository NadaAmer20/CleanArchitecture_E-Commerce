package com.example.demo.application.services;

import com.example.demo.application.dto.UserDto;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import com.example.demo.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public List<UserDto> getAllUsers(String search) {
        logger.info("Fetching all users with search keyword: {}", search);

        List<UserDto> users = userRepository.findAll().stream()
                .filter(user -> isMatchingSearch(user, search))
                .map(this::mapToDto)
                .toList();

        logger.info("Found {} users matching the search.", users.size());
        return users;
    }

    public UserDto getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new RuntimeException("User not found with id: " + id);
                });

        logger.info("User found: {}", user.getUsername());
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

        logger.debug("Mapping user to DTO: {}", user.getUsername());
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roleNames
        );
    }
}
