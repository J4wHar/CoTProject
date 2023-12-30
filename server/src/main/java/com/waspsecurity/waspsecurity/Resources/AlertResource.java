package com.waspsecurity.waspsecurity.Resources;

import com.waspsecurity.waspsecurity.entities.Alert;
import com.waspsecurity.waspsecurity.entities.Coordinates;
import com.waspsecurity.waspsecurity.entities.User;
import com.waspsecurity.waspsecurity.filters.Secured;
import com.waspsecurity.waspsecurity.repositories.AlertRepository;
import com.waspsecurity.waspsecurity.repositories.CoordinatesRepository;
import com.waspsecurity.waspsecurity.repositories.UserRepository;
import com.waspsecurity.waspsecurity.repositories.UserTokenRepository;
import com.waspsecurity.waspsecurity.entities.UserToken;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/alerts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlertResource {
    @Inject
    CoordinatesRepository coordinatesRepository;
    @Inject
    UserTokenRepository userTokenRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    AlertRepository alertRepository;

    @Secured
    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public Response scanForAlerts(@HeaderParam("Authorization") String authHeader) {
        try {
            String userEmail = extractUserEmailFromToken(authHeader);
            List<Alert> alertsToSend = sendAlertsIfApplicable(userEmail);
            return Response.ok(alertsToSend).build();
        } catch (Exception ex) {
            return Response.status(400, ex.getMessage()).build();
        }
    }
    private  String extractUserEmailFromToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Optional<UserToken> user = userTokenRepository.findByAccessToken(token);
            return user.get().getEmail();
        }
        throw new NotAuthorizedException("Bad Token Format");
    }
    private List<Alert> sendAlertsIfApplicable(String userEmail){
        List<Alert> alertsToSend = new ArrayList<>();
        List<User> users = userRepository.findAll().collect(Collectors.toList());
        List<Alert> alerts = alertRepository.findAll().collect(Collectors.toList());
        List<Alert> abnormalAlerts = alerts.stream()
                .filter(alert -> alert.getAbnormalBehaviour() == 1)
                .collect(Collectors.toList());

        for (Alert alert : abnormalAlerts) {
            // Find the nearest 3 users to the alert
            List<User> nearestUsers = findNearestUsers(alert, users, 3);

            boolean userIncluded = nearestUsers.stream()
                    .anyMatch(user -> user.getEmail().equals(userEmail));

            if (userIncluded) {
                alertsToSend.add(alert);
            }
        }
        return alertsToSend;
    }

    // Method to find the nearest N users to an alert based on location
    private List<User> findNearestUsers(Alert alert, List<User> users, int n) {
        return users.stream()
                .sorted(Comparator.comparingDouble(user -> calculateDistance(alert, user)))
                .limit(n)
                .collect(Collectors.toList());
    }

    // Method to calculate distance between an alert and a user based on their locations
    private double calculateDistance(Alert alert, User user) {
        Coordinates userCoord  = coordinatesRepository.findByEmail(user.getEmail()).get();
        double lat1 = alert.getLatitude();
        double lon1 = alert.getLongitude();
        double lat2 = userCoord.getLatitude();
        double lon2 = userCoord.getLongitude();

        return Math.abs(lat1 - lat2) + Math.abs(lon1 - lon2);
    }
}