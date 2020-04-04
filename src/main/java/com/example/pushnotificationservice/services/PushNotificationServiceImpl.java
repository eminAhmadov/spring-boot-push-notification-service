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
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
            String travelerName = pushNotification.get("travelerName");
            String travelOrigin = pushNotification.get("travelOrigin");
            String travelDestination = pushNotification.get("travelDestination");
            String travelDate = pushNotification.get("travelDate");
            User user = userId == null ? null : userService.getByUserId(userId).orElse(null);

            // defining message format EN
            String message =
                    "'{'" +
                            "\"en\": \"{0} is travelling from {1} to {2} on {3}.\"\n" +
                            "'}'";
            MessageFormat contentsFormat = new MessageFormat(message);
            Object[] contentsArgs = {
                    travelerName,
                    travelOrigin,
                    travelDestination,
                    travelDate
            };
            String formattedContents = contentsFormat.format(contentsArgs);
            // defining data for push notification
            String data = "{" +
                    "\"travelerName\": \"" + travelerName + "\",\n" +
                    "\"travelOrigin\": \"" + travelOrigin + "\",\n" +
                    "\"travelDestination\": \"" + travelDestination + "\",\n" +
                    "\"travelDate\": \"" + travelDate + "\"" +
                    "}";

            // making OneSignal API call for sending notification
            String pushNotResponse = null;
            try {
                pushNotResponse = oneSignalService.sendToUserWithUserId(formattedContents, data, user.getUserPushNotId().toString());
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
            pushNotificationToSave.setTravelerName(travelerName);
            pushNotificationToSave.setTravelOrigin(travelOrigin);
            pushNotificationToSave.setTravelDestination(travelDestination);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            pushNotificationToSave.setTravelDate(dateFormat.parse(travelDate));
            return pushNotificationRepository.save(pushNotificationToSave);
        }
        return null;
    }
}
