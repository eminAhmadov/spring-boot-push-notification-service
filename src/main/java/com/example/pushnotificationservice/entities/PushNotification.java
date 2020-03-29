package com.example.pushnotificationservice.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "pushNotId")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "segment"})
@Entity
@Table(name = "push_notifications")
public class PushNotification {

    @Id
    @Type(type = "uuid-char")
    @Column(name = "push_not_id", nullable = false, columnDefinition = "BINARY(36)")
    private UUID pushNotId;

    @OrderBy
    @Column(name = "created_on",
            nullable = false,
            updatable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "traveler_name", nullable = false)
    private String travelerName;

    @Column(name = "travel_origin", nullable = false)
    private String travelOrigin;

    @Column(name = "travel_destination", nullable = false)
    private String travelDestination;

    @Column(name = "travel_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date travelDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PushNotification() {
    }

    public PushNotification(UUID pushNotId, LocalDateTime createdOn) {
        this.pushNotId = pushNotId;
        this.createdOn = createdOn;
    }

    public PushNotification(String pushNotId, LocalDateTime createdOn) {
        this.pushNotId = UUID.fromString(pushNotId);
        this.createdOn = createdOn;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public UUID getPushNotId() {
        return this.pushNotId;
    }

    public void setPushNotId(UUID pushNotId) {
        this.pushNotId = pushNotId;
    }

    public void setPushNotId(String pushNotId) {
        this.pushNotId = UUID.fromString(pushNotId);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTravelerName() {
        return travelerName;
    }

    public void setTravelerName(String travelerName) {
        this.travelerName = travelerName;
    }

    public String getTravelOrigin() {
        return travelOrigin;
    }

    public void setTravelOrigin(String travelOrigin) {
        this.travelOrigin = travelOrigin;
    }

    public String getTravelDestination() {
        return travelDestination;
    }

    public void setTravelDestination(String travelDestination) {
        this.travelDestination = travelDestination;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }
}
