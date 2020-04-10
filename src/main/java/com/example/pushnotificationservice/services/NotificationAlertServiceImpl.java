package com.example.pushnotificationservice.services;

import com.example.pushnotificationservice.entities.NotificationAlert;
import com.example.pushnotificationservice.entities.PushNotification;
import com.example.pushnotificationservice.entities.User;
import com.example.pushnotificationservice.repositories.NotificationAlertRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class NotificationAlertServiceImpl implements NotificationAlertService {

    final
    NotificationAlertRepository notificationAlertRepository;
    final
    UserService userService;
    final
    PushNotificationService pushNotificationService;

    public NotificationAlertServiceImpl(
            NotificationAlertRepository notificationAlertRepository,
            UserService userService,
            PushNotificationService pushNotificationService
    ) {
        this.notificationAlertRepository = notificationAlertRepository;
        this.userService = userService;
        this.pushNotificationService = pushNotificationService;
    }

    @Override
    public List<NotificationAlert> getAll(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdOn"));
        return notificationAlertRepository.findAll(pageable).getContent();
    }

    @Override
    public List<NotificationAlert> getAllForUserWithUserId(UUID userId, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdOn"));
        return notificationAlertRepository.findByUser_UserId(userId, pageable).getContent();
    }

    @Override
    public List<PushNotification> triggerMatchingAlerts(String travelerName, String travelOrigin, String travelDestination, Date travelDate) throws ParseException {
        List<User> foundUsers = notificationAlertRepository.findUsersWithMatchingAlert(travelOrigin, travelDestination, travelDate);
        List<PushNotification> sentNotifications = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, String> pushNotificationBody = new HashMap<String, String>() {{
            put("travelerName", travelerName);
            put("travelOrigin", travelOrigin);
            put("travelDestination", travelDestination);
            put("travelDate", dateFormat.format(travelDate));
        }};
        for (User user : foundUsers) {
            PushNotification pushNotification = pushNotificationService.createPushNotification(user.getUserId(), pushNotificationBody);
            sentNotifications.add(pushNotification);
        }
        return sentNotifications;
    }

    @Override
    public NotificationAlert createNotificationAlert(UUID userId, Map<String, String> notificationAlert) throws ParseException {
        User user = userId == null ? null : userService.getByUserId(userId).orElse(null);
        if (user != null) {
            String travelOrigin = notificationAlert.get("travelOrigin");
            String travelDestination = notificationAlert.get("travelDestination");
            String travelDateFrom = notificationAlert.get("travelDateFrom");
            String travelDateTo = notificationAlert.get("travelDateTo");

            NotificationAlert notificationAlertToSave = new NotificationAlert();
            notificationAlertToSave.setUser(user);
            notificationAlertToSave.setTravelOrigin(travelOrigin);
            notificationAlertToSave.setTravelDestination(travelDestination);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            notificationAlertToSave.setTravelDateFrom(dateFormat.parse(travelDateFrom));
            notificationAlertToSave.setTravelDateTo(dateFormat.parse(travelDateTo));

            return notificationAlertRepository.save(notificationAlertToSave);
        }
        return null;
    }

}
