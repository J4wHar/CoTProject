package com.waspsecurity.waspsecurity.models;

import com.waspsecurity.waspsecurity.FieldPropertyVisibilityStrategy;
import com.waspsecurity.waspsecurity.entities.AccessToken;
import com.waspsecurity.waspsecurity.entities.RefreshToken;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbVisibility;


@JsonbVisibility(FieldPropertyVisibilityStrategy.class)
public class Oauth2Response {
    @JsonbProperty("access_token")
    private String accessToken;

    @JsonbProperty("expires_in")
    private int expiresIn;

    @JsonbProperty("employeeId")
    private String employeeId;

    @JsonbProperty("refresh_token")
    private String refreshToken;


    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public static Oauth2Response of(AccessToken accessToken, RefreshToken refreshToken, int expiresIn, String employeeId) {
        Oauth2Response response = new Oauth2Response();
        response.accessToken = accessToken.getToken();
        response.refreshToken = refreshToken.getToken();
        response.employeeId = employeeId;
        response.expiresIn = expiresIn;
        return response;
    }
}
