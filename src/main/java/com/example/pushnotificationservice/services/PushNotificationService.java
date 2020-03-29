package com.example.pushnotificationservice.services;

import com.example.pushnotificationservice.entities.PushNotification;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PushNotificationService {
    List<PushNotification> getAll(int offset, int limit);
    List<PushNotification> getAllForUserWithUserId(UUID userId, int offset, int limit);
    PushNotification createPushNotification(UUID userId, Map<String, String> pushNotification) throws ParseException;
}
