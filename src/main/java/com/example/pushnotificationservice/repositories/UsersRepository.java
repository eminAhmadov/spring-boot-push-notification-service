package com.example.pushnotificationservice.repositories;

import com.example.pushnotificationservice.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsersRepository extends CrudRepository<User, UUID> {
    List<User> findAll();

    @Query(value = "SELECT user_id FROM users", nativeQuery = true)
    List<UUID> findUserIds();

    User findByUserId(UUID userId);

}