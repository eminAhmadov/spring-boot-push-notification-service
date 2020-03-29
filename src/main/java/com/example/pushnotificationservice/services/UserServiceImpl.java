package com.example.pushnotificationservice.services;

import com.example.pushnotificationservice.entities.User;
import com.example.pushnotificationservice.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    final
    UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User add(User user) {
        return usersRepository.save(user);
    }

    @Override
    public Optional<User> getByUserId(UUID userId) {
        return usersRepository.findById(userId);
    }

    @Override
    public List<UUID> getAllUserIds() {
        return usersRepository.findUserIds();
    }
}
