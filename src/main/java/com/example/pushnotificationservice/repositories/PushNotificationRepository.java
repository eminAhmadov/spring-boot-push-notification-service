package com.example.pushnotificationservice.repositories;

import com.example.pushnotificationservice.entities.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PushNotificationRepository extends PagingAndSortingRepository<PushNotification, UUID> {
    Page<PushNotification> findAll(Pageable pageable);
    Page<PushNotification> findByUser_UserId(UUID userId, Pageable pageable);
}
