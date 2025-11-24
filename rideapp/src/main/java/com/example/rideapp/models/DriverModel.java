package com.example.rideapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class DriverModel {

    @Id
    private String email; 

    private String status = "pending";

    private String licenseNumber;
    private String licenseImage;
    private String registrationImage;
    private String carModel;
    private String carColor;
    private String carPlate;
    private int seatsAvailable;

    public DriverModel() {
    }

    public DriverModel(String email, String licenseNumber,
            String licenseImage, String registrationImage,
            String carModel, String carColor,
            String carPlate, int seatsAvailable) {
        this.email = email;
        this.licenseNumber = licenseNumber;
        this.licenseImage = licenseImage;
        this.registrationImage = registrationImage;
        this.carModel = carModel;
        this.carColor = carColor;
        this.carPlate = carPlate;
        this.seatsAvailable = seatsAvailable;
        this.status = "pending";
    }


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

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
