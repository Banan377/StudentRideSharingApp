package com.example.rideapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "passengers")
public class PassengerModel {

    @Id
    private String email; 

    private int totalRides = 0;
    private double rating = 5.0;

    public PassengerModel() {}

    public PassengerModel(String email) {
        this.email = email;
        this.totalRides = 0;
        this.rating = 5.0;
    }

    // Getters & Setters

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getTotalRides() { return totalRides; }
    public void setTotalRides(int totalRides) { this.totalRides = totalRides; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}
