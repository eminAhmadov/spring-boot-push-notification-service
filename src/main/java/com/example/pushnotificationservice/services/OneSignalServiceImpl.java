package com.example.pushnotificationservice.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class OneSignalServiceImpl implements OneSignalService {

    private static final String REST_API_KEY = System.getenv("ONE_SIGNAL_REST_API_KEY");
    private static final String APP_ID = System.getenv("ONE_SIGNAL_APP_ID");

    public String sendToAll(String contents, String data) throws Exception {
        String strJsonBody = "{"
                + "\"app_id\": \"" + APP_ID + "\","
                + "\"included_segments\": [\"All\"],"
                + "\"data\": " + data + ","
                + "\"contents\": " + contents
                + "}";
        return createNotification(strJsonBody);
    }

    public String sendToUserWithUserId(String contents, String data, String userPushNotId) throws Exception {
        String strJsonBody = "{"
                + "\"app_id\": \"" + APP_ID + "\","
                + "\"include_player_ids\": [\"" + userPushNotId + "\"],"
                + "\"data\": " + data + ","
                + "\"contents\": " + contents
                + "}";
        return createNotification(strJsonBody);
    }

    private static String createNotification(String strJsonBody) throws Exception {
        String jsonResponse;

        URL url = new URL("https://onesignal.com/api/v1/notifications");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);

        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Authorization",
                "Basic " + REST_API_KEY);
        con.setRequestMethod("POST");

        System.out.println("strJsonBody:\n" + strJsonBody);

        byte[] sendBytes = strJsonBody.getBytes(StandardCharsets.UTF_8);
        con.setFixedLengthStreamingMode(sendBytes.length);

        OutputStream outputStream = con.getOutputStream();
        outputStream.write(sendBytes);

        int httpResponse = con.getResponseCode();
        System.out.println("httpResponse: " + httpResponse);

        jsonResponse = mountResponseRequest(con, httpResponse);

        System.out.println("jsonResponse:\n" + jsonResponse);

        return jsonResponse;
    }

    private static String mountResponseRequest(HttpURLConnection con, int httpResponse) throws IOException {
        String jsonResponse;
        if (httpResponse >= HttpURLConnection.HTTP_OK
                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        } else {
            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            scanner.close();
        }
        return jsonResponse;
    }
}
