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
    private Long longitude;
    @Column
    private Long latitude;

    public Long getLongitude() {
        return longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }
}
