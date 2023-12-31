package com.waspsecurity.waspsecurity.entities;



import jakarta.nosql.Column;
import jakarta.nosql.Entity;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class AccessToken {
    @Column
    private String token;
    @Column
    private String jwtSecret;
    @Column
    private LocalDateTime expiration;


    public AccessToken() {
    }

    public AccessToken(String token, String jwtSecret, Duration duration) {
        this.token = token;
        this.jwtSecret = jwtSecret;
        this.expiration = LocalDateTime.now().plus(duration);
    }

    public String getToken() {
        return token;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }
    public boolean isValid() {
        final LocalDateTime now = LocalDateTime.now();
        return now.isBefore(expiration);
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "token='" + token + '\'' +
                ", jwtSecret='" + jwtSecret + '\'' +
                ", expiration=" + expiration +
                '}';
    }
}
