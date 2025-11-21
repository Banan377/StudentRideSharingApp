package com.example.rideapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "passengers")
public class PassengerModel extends UserModel {
    @Id
    private String email;

    private int totalRides;
    private double rating;

    // Constructors
    public PassengerModel() {}

    public PassengerModel(String email) {
        this.email = email;
        this.totalRides = 0;
        this.rating = 5.0;
    }

    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
     
    public int getTotalRides() { return totalRides; }
    public void setTotalRides(int totalRides) { this.totalRides = totalRides; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}