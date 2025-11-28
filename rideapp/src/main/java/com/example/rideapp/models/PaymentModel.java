package com.example.rideapp.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "amount", nullable = false)
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @ManyToOne
    @JoinColumn(name = "passenger_email", referencedColumnName = "email", nullable = false)
    private UserModel passenger;
    
    @ManyToOne
    @JoinColumn(name = "driver_email", referencedColumnName = "email", nullable = false)
    private UserModel driver;
    
    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    private RideModel ride;

    public enum PaymentMethod {
        CASH, CREDIT_CARD, MOBILE_WALLET
    }
    
    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED, REFUNDED
    }

    public PaymentModel() {}
    
    public PaymentModel(Double amount, PaymentMethod paymentMethod, PaymentStatus paymentStatus, 
                       LocalDateTime paymentDate, UserModel passenger, UserModel driver, RideModel ride) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.passenger = passenger;
        this.driver = driver;
        this.ride = ride;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public UserModel getPassenger() { return passenger; }
    public void setPassenger(UserModel passenger) { this.passenger = passenger; }
    
    public UserModel getDriver() { return driver; }
    public void setDriver(UserModel driver) { this.driver = driver; }
    
    public RideModel getRide() { return ride; }
    public void setRide(RideModel ride) { this.ride = ride; }
}