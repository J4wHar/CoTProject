package com.waspsecurity.waspsecurity.entities;

import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Alert {
     @Id
    @Column
    private String timestamp;
      @Column
      private byte abnormalBehaviour;

      @Column
      private double latitude;
      @Column
      private double longitude;


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public byte getAbnormalBehaviour() {
        return abnormalBehaviour;
    }

    public void setAbnormalBehaviour(byte abnormalBehaviour) {
        this.abnormalBehaviour = abnormalBehaviour;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String  timestamp) {
        this.timestamp = timestamp;
    }
}
