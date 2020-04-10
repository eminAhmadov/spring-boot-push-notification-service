package com.example.pushnotificationservice.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Date;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "user", "segment"})
@Entity
@Table(name = "notification_alerts")
public class NotificationAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "travel_origin", nullable = false)
    private String travelOrigin;

    @Column(name = "travel_destination", nullable = false)
    private String travelDestination;

    @Column(name = "travel_date_from", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date travelDateFrom;

    @Column(name = "travel_date_to", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date travelDateTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public NotificationAlert() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getTravelDateFrom() {
        return travelDateFrom;
    }

    public void setTravelDateFrom(Date travelDateFrom) {
        this.travelDateFrom = travelDateFrom;
    }

    public Date getTravelDateTo() {
        return travelDateTo;
    }

    public void setTravelDateTo(Date travelDateTo) {
        this.travelDateTo = travelDateTo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
