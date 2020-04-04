package com.example.pushnotificationservice.controllers;

import com.example.pushnotificationservice.entities.PushNotification;
import com.example.pushnotificationservice.services.PushNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("pushNotification")
public class PushNotificationController {

    final
    PushNotificationService pushNotificationService;

    public PushNotificationController(
            PushNotificationService pushNotificationService
    ) {
        this.pushNotificationService = pushNotificationService;
    }

    @GetMapping("/getAll")
    public List<PushNotification> getAll(
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "limit", defaultValue = "5", required = false) int limit
    ) {
        return pushNotificationService.getAll(offset, limit);
    }

    @GetMapping("/getAllForUserWithUserId")
    public List<PushNotification> getAllForUserWithUserId(
            @RequestParam UUID userId,
            @RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
            @RequestParam(value = "limit", defaultValue = "5", required = false) int limit
    ) {
        return pushNotificationService.getAllForUserWithUserId(userId, offset, limit);
    }

    @PostMapping(path = "/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity<PushNotification> sendPushNotification(
            @RequestParam UUID userId,
            @RequestBody Map<String, String> pushNotification) throws ParseException {
        PushNotification createdPushNotification = pushNotificationService.createPushNotification(userId, pushNotification);
        if (createdPushNotification == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdPushNotification, HttpStatus.OK);
    }

}
