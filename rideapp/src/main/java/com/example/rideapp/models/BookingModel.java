package com.example.rideapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class BookingModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;
    @ManyToOne
    @JoinColumn(name = "ride_id")
    private RideModel ride;
  

    @ManyToOne
    @JoinColumn(name = "passenger_email")
    private PassengerModel passenger;

    private String paymentStatus;
    private String status; // إضافة الحالة: pending, accepted, rejected

    public BookingModel() {
    }

    // Getters and Setters
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public RideModel getRide() {
        return ride;
    }

    public void setRide(RideModel ride) {
        this.ride = ride;
    }

    public PassengerModel getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerModel passenger) {
        this.passenger = passenger;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}