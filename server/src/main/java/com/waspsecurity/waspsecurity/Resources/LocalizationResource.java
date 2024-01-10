package com.waspsecurity.waspsecurity.Resources;

import com.waspsecurity.waspsecurity.entities.Coordinates;
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
@Path("/localization")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LocalizationResource {
    @Inject
    CoordinatesRepository coordinatesRepository;
    @Inject
    UserTokenRepository userTokenRepository;
    @Secured
    @GET
    @RolesAllowed({"USER"})
    public Response getUserLocalization(@HeaderParam("Authorization") String authHeader) {
        try{
            Coordinates coordinates = coordinatesRepository.findByEmail(extractUserEmailFromToken(authHeader)).get();
            return Response.ok(coordinates).build();
        }
        catch(Exception ex){
            return   Response.status(400, ex.getMessage()).build();
        }
    }
    @Secured
    @POST
    @RolesAllowed({"USER"})
    public Response AddUserLocalization(@HeaderParam("Authorization") String authHeader, Coordinates coordinates) {
        try{
            String userEmail = extractUserEmailFromToken(authHeader);
            coordinates.setEmail(userEmail);
            coordinatesRepository.save(coordinates);
            return Response.ok("Localization Added Successfully!").build();
        }
       catch (Exception ex){
            return   Response.status(400, ex.getMessage()).build();
       }
    }
    @Secured
    @PUT
    @RolesAllowed({"USER"})
    public Response UpdateUserLocalization(@HeaderParam("Authorization") String authHeader, Coordinates coordinates) {
        try{
            String userEmail = extractUserEmailFromToken(authHeader);
            Coordinates existingCoordinate = coordinatesRepository.findByEmail(userEmail).get();
            coordinates.setEmail(existingCoordinate.getEmail());
            coordinatesRepository.save(coordinates);
            return Response.ok( "Localization Updated Successfully!").build();
        }
        catch (Exception ex){
            return   Response.status(400, ex.getMessage()).build();
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
