package com.example.rideapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class DriverModel {
    @Id
    private String email;

    private String licenseNumber;
    private String licenseImage;
    private String registrationImage;
    private String carModel;
    private String carColor;
    private String carPlate;
    private String status; // pending, approved, rejected
    private int seatsAvailable;

    // Constructors
    public DriverModel() {
    }

    public DriverModel(String email, String licenseNumber, String licenseImage,
            String registrationImage, String carModel, String carColor,
            String carPlate, int seatsAvailable) {
        this.email = email;
        this.licenseNumber = licenseNumber;
        this.licenseImage = licenseImage;
        this.registrationImage = registrationImage;
        this.carModel = carModel;
        this.carColor = carColor;
        this.carPlate = carPlate;
        this.status = "pending";
        this.seatsAvailable = seatsAvailable;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseImage() {
        return licenseImage;
    }

    public void setLicenseImage(String licenseImage) {
        this.licenseImage = licenseImage;
    }

    public String getRegistrationImage() {
        return registrationImage;
    }

    public void setRegistrationImage(String registrationImage) {
        this.registrationImage = registrationImage;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }
}