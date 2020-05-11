package com.example.pushnotificationservice.controllers;

import com.example.pushnotificationservice.entities.PushNotification;
import com.example.pushnotificationservice.services.PushNotificationService;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import com.example.pushnotificationservice.entities.NotificationAlert;
import com.example.pushnotificationservice.services.NotificationAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationAlertController.class)
class NotificationAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationAlertService service;

    @Test
    void getAll() throws Exception {
        NotificationAlert notificationAlert1 = new NotificationAlert();
        notificationAlert1.setTravelOrigin("Toronto");
        notificationAlert1.setTravelDestination("Baku");
        NotificationAlert notificationAlert2 = new NotificationAlert();
        notificationAlert2.setTravelOrigin("Baku");
        notificationAlert2.setTravelDestination("Toronto");

        List<NotificationAlert> notificationAlerts = Arrays.asList(
                notificationAlert1, notificationAlert2
        );
        when(service.getAll(0, 10)).thenReturn(notificationAlerts);

        mockMvc.perform(get("/notificationAlert/getAll")
                .param("offset", "0")
                .param("limit", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].travelOrigin").value("Toronto"))
                .andExpect(jsonPath("$[1].travelOrigin").value("Baku"))
                .andExpect(jsonPath("$[0].travelDestination").value("Baku"))
                .andExpect(jsonPath("$[1].travelDestination").value("Toronto"));

        verify(service, times(1)).getAll(0, 10);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllForUserWithUserId() throws Exception {
        NotificationAlert notificationAlert = new NotificationAlert();
        notificationAlert.setTravelOrigin("Toronto");
        notificationAlert.setTravelDestination("Baku");

        List<NotificationAlert> notificationAlerts = Arrays.asList(notificationAlert);

        when(service.getAllForUserWithUserId(UUID.fromString("c175afac-480e-4d08-8f97-92b822febea6"), 0, 10))
                .thenReturn(notificationAlerts);

        mockMvc.perform(get("/notificationAlert/getAllForUserWithUserId")
                .param("userId", "c175afac-480e-4d08-8f97-92b822febea6")
                .param("offset", "0")
                .param("limit", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].travelOrigin").value("Toronto"))
                .andExpect(jsonPath("$[0].travelDestination").value("Baku"));

        verify(service, times(1))
                .getAllForUserWithUserId(UUID.fromString("c175afac-480e-4d08-8f97-92b822febea6"), 0, 10);
        verifyNoMoreInteractions(service);
    }
}