package com.example.rideapp.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "safety")
public class SafetyModel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saftyId;

    @ManyToOne
    @JoinColumn(name = "rideId")
    private RideModel ride;

    @ManyToOne
    @JoinColumn(name = "triggeredBy")
    private UserModel triggeredBy;

    private String status;      // TRIGGERED, NOTIFIED, RESOLVED
    private String message;        // رسالة الطوارئ المرسلة
    private Double latitude;       // خط العرض عند حدوث الطوارئ
    private Double longitude;      // خط الطول عند حدوث الطوارئ
    private LocalDateTime createdAt; // وقت إنشاء التنبيه

    public SafetyModel() {

    }

    public void setSaftyId(Long saftyId) {
        this.saftyId = saftyId;
    }
    public Long getSaftyId() {
        return saftyId;
    }
    public void setRide(RideModel ride) {
        this.ride = ride;
    }
    public RideModel getRide() {
        return ride;
    }
    public void setTriggeredBy(UserModel triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
    public UserModel getTriggeredBy() {
        return triggeredBy;
    }
    public void setstatus(String status) {
        this.status = status;
    }
    public String getstatus() {
        return status;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
