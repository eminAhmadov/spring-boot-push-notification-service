package com.example.pushnotificationservice.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userId")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "users")
public class User {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "user_id", nullable = false, columnDefinition = "BINARY(36)")
    private UUID userId;

    @Type(type="uuid-char")
    @Column(name = "user_push_not_id", nullable = false, columnDefinition = "BINARY(36)")
    private UUID userPushNotId;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PushNotification> pushNotifications = new ArrayList<>();

    @Column(name = "added_on",
            nullable = false,
            updatable = false)
    @JsonIgnore
    private LocalDateTime addedOn;

    public User() {
        this.addedOn = LocalDateTime.now();
    }

    public User(UUID userId, UUID userPushNotId) {
        this.userId = userId;
        this.userPushNotId = userPushNotId;
        this.addedOn = LocalDateTime.now();
    }

    public User(UUID userId, String userPushNotId) {
        this.userId = userId;
        this.userPushNotId = UUID.fromString(userPushNotId);
        this.addedOn = LocalDateTime.now();
    }

    public User(String userId, UUID userPushNotId) {
        this.userId = UUID.fromString(userId);
        this.userPushNotId = userPushNotId;
        this.addedOn = LocalDateTime.now();
    }

    public User(String userId, String userPushNotId) {
        this.userId = UUID.fromString(userId);
        this.userPushNotId = UUID.fromString(userPushNotId);
        this.addedOn = LocalDateTime.now();
    }

    public UUID getUserId() {
        return this.userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = UUID.fromString(userId);
    }

    public UUID getUserPushNotId() {
        return this.userPushNotId;
    }

    public void setUserPushNotId(UUID userPushNotId) {
        this.userPushNotId = userPushNotId;
    }

    public void setUserPushNotId(String userPushNotId) {
        this.userPushNotId = UUID.fromString(userPushNotId);
    }

    public List<PushNotification> getPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(List<PushNotification> pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime addedOn) {
        this.addedOn = addedOn;
    }

}