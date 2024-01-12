package com.waspsecurity.waspsecurity.entities;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

@Entity
public class Coordinates {
    @Id
    @Column
    private String email;
    @Column
    private double longitude;
    @Column
    private double latitude;

    public double getLongitude() {
        return longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
