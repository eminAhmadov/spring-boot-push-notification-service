package com.example.pushnotificationservice.services;

public interface OneSignalService {
    String sendToAll(String contents, String data) throws Exception;
    String sendToUserWithUserId(String contents, String data, String userPushNotId) throws Exception;
}
