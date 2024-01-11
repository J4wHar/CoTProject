package com.waspsecurity.waspsecurity.models;

import com.waspsecurity.waspsecurity.FieldPropertyVisibilityStrategy;
import com.waspsecurity.waspsecurity.entities.AccessToken;
import com.waspsecurity.waspsecurity.entities.RefreshToken;
import com.waspsecurity.waspsecurity.enums.Role;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbVisibility;

import java.util.Set;


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

    @JsonbProperty("roles")
    private Set<Role> roles;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public static Oauth2Response of(AccessToken accessToken,
                                    RefreshToken refreshToken,
                                    int expiresIn,
                                    String employeeId,
                                    Set<Role> roles) {
        Oauth2Response response = new Oauth2Response();
        response.accessToken = accessToken.getToken();
        response.refreshToken = refreshToken.getToken();
        response.employeeId = employeeId;
        response.expiresIn = expiresIn;
        response.roles = roles;
        return response;
    }
}
