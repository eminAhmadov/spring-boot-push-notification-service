package com.example.pushnotificationservice.repositories;

import com.example.pushnotificationservice.entities.NotificationAlert;
import com.example.pushnotificationservice.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationAlertRepository extends PagingAndSortingRepository<NotificationAlert, Long> {
    Page<NotificationAlert> findAll(Pageable pageable);
    Page<NotificationAlert> findByUser_UserId(UUID userId, Pageable pageable);

    @Query("SELECT notificationAlert.user FROM NotificationAlert notificationAlert WHERE notificationAlert.travelOrigin = :travelOrigin AND notificationAlert.travelDestination = :travelDestination AND :travelDate BETWEEN notificationAlert.travelDateFrom AND notificationAlert.travelDateTo")
    List<User> findUsersWithMatchingAlert(String travelOrigin, String travelDestination, Date travelDate);
}
