package com.example.pushnotificationservice.services;

import com.example.pushnotificationservice.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User add(User user);

    Optional<User> getByUserId(UUID userId);
}
