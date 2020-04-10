package com.example.pushnotificationservice.controllers;

import com.example.pushnotificationservice.entities.NotificationAlert;
import com.example.pushnotificationservice.entities.PushNotification;
import com.example.pushnotificationservice.services.NotificationAlertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("notificationAlert")
public class NotificationAlertController {

    final
    NotificationAlertService notificationAlertService;

    public NotificationAlertController(
            NotificationAlertService notificationAlertService
    ) {
        this.notificationAlertService = notificationAlertService;
    }

    @GetMapping("/getAll")
    public List<NotificationAlert> getAll(
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "limit", defaultValue = "5", required = false) int limit
    ) {
        return notificationAlertService.getAll(offset, limit);
    }

    @GetMapping("/getAllForUserWithUserId")
    public List<NotificationAlert> getAllForUserWithUserId(
            @RequestParam UUID userId,
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "limit", defaultValue = "5", required = false) int limit
    ) {
        return notificationAlertService.getAllForUserWithUserId(userId, offset, limit);
    }

    @PostMapping(path = "/trigger")
    public List<PushNotification> triggerMatchingAlerts(
            @RequestParam String travelerName,
            @RequestParam String travelOrigin,
            @RequestParam String travelDestination,
            @RequestParam Date travelDate
    ) throws ParseException {
        return notificationAlertService.triggerMatchingAlerts(travelerName, travelOrigin, travelDestination, travelDate);
    }

    @PostMapping(path = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<NotificationAlert> createNotificationAlert(
            @RequestParam UUID userId,
            @RequestBody Map<String, String> notificationAlert) throws ParseException {
        NotificationAlert createdNotificationAlert = notificationAlertService.createNotificationAlert(userId, notificationAlert);
        if (createdNotificationAlert == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdNotificationAlert, HttpStatus.OK);
    }

}
