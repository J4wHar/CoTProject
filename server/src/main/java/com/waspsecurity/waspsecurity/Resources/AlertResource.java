package com.waspsecurity.waspsecurity.Resources;

import com.waspsecurity.waspsecurity.filters.Secured;
import com.waspsecurity.waspsecurity.repositories.CoordinatesRepository;
import com.waspsecurity.waspsecurity.repositories.UserTokenRepository;
import com.waspsecurity.waspsecurity.entities.UserToken;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@ApplicationScoped
@Path("/alerts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlertResource {
    @Inject
    CoordinatesRepository coordinatesRepository;
    @Inject
    UserTokenRepository userTokenRepository;

    @Secured
    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public Response scanForAlerts(@HeaderParam("Authorization") String authHeader) {
        try {
            // we have to scan for alerts, and if they exist we need to calculate the three nearest persons in our  users collection
            //  maybe we have to think about alerts occured in the last 4- 10 days only
            return Response.ok().build();
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
}