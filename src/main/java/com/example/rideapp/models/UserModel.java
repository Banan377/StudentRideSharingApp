
package com.example.rideapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    protected String email;

    protected String name;
    protected String password;
    protected String role;
    protected String college;
    protected String gender;
    protected String emergencyContacts;
    protected double rateAverage;
    protected String status;
    private String currentLocation; // "lat,lng"

    public UserModel() {
    }

    public UserModel(String email, String name, String password, String role, String college,
            String gender, String emergencyContacts, double rateAverage, String status) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.college = college;
        this.gender = gender;
        this.emergencyContacts = emergencyContacts;
        this.rateAverage = rateAverage;
        this.status = status;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(String emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    public double getRateAverage() {
        return rateAverage;
    }

    public void setRateAverage(double rateAverage) {
        this.rateAverage = rateAverage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getCurrentLocation() {
        return currentLocation;
    }
    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}