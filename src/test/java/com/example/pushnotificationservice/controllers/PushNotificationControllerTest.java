package com.example.pushnotificationservice.controllers;

import com.example.pushnotificationservice.entities.PushNotification;
import com.example.pushnotificationservice.services.PushNotificationService;
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

@WebMvcTest(PushNotificationController.class)
class PushNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PushNotificationService service;

    @Test
    void getAll() throws Exception {
        PushNotification pushNotification1 = new PushNotification();
        pushNotification1.setPushNotId("c175afac-480e-4d08-8f97-92b822febea6");
        pushNotification1.setTravelerName("Emin Ahmadov");
        PushNotification pushNotification2 = new PushNotification();
        pushNotification2.setPushNotId("a937e35f-09aa-462c-a6c8-0264f9e153de");
        pushNotification2.setTravelerName("Lala Ahmadova");

        List<PushNotification> pushNotifications = Arrays.asList(
                pushNotification1, pushNotification2
        );
        when(service.getAll(0, 10)).thenReturn(pushNotifications);

        mockMvc.perform(get("/pushNotification/getAll")
                .param("offset", "0")
                .param("limit", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].pushNotId").value("c175afac-480e-4d08-8f97-92b822febea6"))
                .andExpect(jsonPath("$[1].pushNotId").value("a937e35f-09aa-462c-a6c8-0264f9e153de"))
                .andExpect(jsonPath("$[0].travelerName").value("Emin Ahmadov"))
                .andExpect(jsonPath("$[1].travelerName").value("Lala Ahmadova"));

        verify(service, times(1)).getAll(0, 10);
        verifyNoMoreInteractions(service);
    }

    @Test
    void getAllForUserWithUserId() throws Exception {
        PushNotification pushNotification = new PushNotification();
        pushNotification.setPushNotId("c175afac-480e-4d08-8f97-92b822febea6");
        pushNotification.setTravelerName("Emin Ahmadov");
        List<PushNotification> travels = Arrays.asList(pushNotification);

        when(service.getAllForUserWithUserId(UUID.fromString("c175afac-480e-4d08-8f97-92b822febea6"), 0, 10))
                .thenReturn(travels);

        mockMvc.perform(get("/pushNotification/getAllForUserWithUserId")
                .param("userId", "c175afac-480e-4d08-8f97-92b822febea6")
                .param("offset", "0")
                .param("limit", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].pushNotId").value("c175afac-480e-4d08-8f97-92b822febea6"))
                .andExpect(jsonPath("$[0].travelerName").value("Emin Ahmadov"));

        verify(service, times(1))
                .getAllForUserWithUserId(UUID.fromString("c175afac-480e-4d08-8f97-92b822febea6"), 0, 10);
        verifyNoMoreInteractions(service);
    }
}