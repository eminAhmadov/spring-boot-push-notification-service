package com.example.pushnotificationservice.services;

import com.example.pushnotificationservice.entities.NotificationAlert;
import com.example.pushnotificationservice.entities.PushNotification;
import com.example.pushnotificationservice.entities.User;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface NotificationAlertService {
    List<NotificationAlert> getAll(int offset, int limit);
    List<NotificationAlert> getAllForUserWithUserId(UUID userId, int offset, int limit);
    List<PushNotification> triggerMatchingAlerts(String travelerName, String travelOrigin, String travelDestination, Date travelDate) throws ParseException;
    NotificationAlert createNotificationAlert(UUID userId, Map<String, String> notificationAlert) throws ParseException;
}
