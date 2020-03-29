package com.example.pushnotificationservice.services;

import com.example.pushnotificationservice.entities.PushNotification;
import com.example.pushnotificationservice.entities.User;
import com.example.pushnotificationservice.repositories.PushNotificationRepository;
import com.example.pushnotificationservice.repositories.UsersRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

@Service
public class PushNotificationServiceImpl implements PushNotificationService {

    final
    PushNotificationRepository pushNotificationRepository;
    final
    UsersRepository usersRepository;
    final
    UserService userService;
    final
    OneSignalService oneSignalService;

    public PushNotificationServiceImpl(
            PushNotificationRepository pushNotificationRepository,
            UsersRepository usersRepository,
            UserService userService,
            OneSignalService oneSignalService
    ) {
        this.pushNotificationRepository = pushNotificationRepository;
        this.usersRepository = usersRepository;
        this.userService = userService;
        this.oneSignalService = oneSignalService;
    }

    @Override
    public List<PushNotification> getAll(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdOn"));
        return pushNotificationRepository.findAll(pageable).getContent();
    }

    @Override
    public List<PushNotification> getAllForUserWithUserId(UUID userId, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createdOn"));
        return pushNotificationRepository.findByUser_UserId(userId, pageable).getContent();
    }

    @Override
    public PushNotification createPushNotification(UUID userId, Map<String, String> pushNotification) throws ParseException {
        // send push notification by one signal
        Optional<User> foundUser = usersRepository.findByUserId(userId);
        if (foundUser.isPresent()) {
            // getting properties from JSON
            String traveler_name = pushNotification.get("traveler_name");
            String travel_origin = pushNotification.get("travel_origin");
            String travel_destination = pushNotification.get("travel_destination");
            String travel_date = pushNotification.get("travel_date");
            User user = userId == null ? null : userService.getByUserId(userId).orElse(null);

            // defining message format EN
            String message =
                    "{" +
                    "\"en\": \"{0} is travelling from {1} to {2} on {3}.\",\n" +
                    "}";
            MessageFormat contentsFormat = new MessageFormat(message);
            Object[] contentsArgs = {
                    traveler_name,
                    travel_origin,
                    travel_destination,
                    travel_date
            };
            String formattedContents = contentsFormat.format(contentsArgs);
            // defining data for push notification
            String data = "{" +
                    "\"traveler_name\": \"" + traveler_name + "\",\n" +
                    "\"travel_origin\": \"" + travel_origin + "\",\n" +
                    "\"travel_destination\": " + travel_destination + ",\n" +
                    "\"travel_date\": \"" + travel_date + "\"" +
                    "}";

            // making OneSignal API call for sending notification
            String pushNotResponse = null;
            try {
                if (user == null) {
                    pushNotResponse = oneSignalService.sendToAll(formattedContents, data);
                } else {
                    pushNotResponse = oneSignalService.sendToUserWithUserId(formattedContents, data, user.getUserPushNotId().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(pushNotResponse);
            String pushNotId = null;
            if (pushNotResponse != null) {
                pushNotId = pushNotResponse.substring(7, 43);
            }

            // create push notification object and return it
            PushNotification pushNotificationToSave = new PushNotification();
            pushNotificationToSave.setPushNotId(pushNotId);
            pushNotificationToSave.setUser(user);
            pushNotificationToSave.setTravelerName(traveler_name);
            pushNotificationToSave.setTravelOrigin(travel_origin);
            pushNotificationToSave.setTravelDestination(travel_destination);
            pushNotificationToSave.setTravelDate(java.text.DateFormat.getDateInstance().parse(travel_date));
            return pushNotificationRepository.save(pushNotificationToSave);
        }
        return null;
    }
}
