package com.waspsecurity.waspsecurity.Resources;


import com.waspsecurity.waspsecurity.security.Oauth2Request;
import com.waspsecurity.waspsecurity.security.Oauth2Response;
import com.waspsecurity.waspsecurity.security.Oauth2Service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("oauth2")
public class Oauth2Ressources {
    @Inject
    Oauth2Service service;

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Oauth2Response token( Oauth2Request request) {
        switch (request.getGrandType()) {
            case PASSWORD:
                return service.token(request);
            case REFRESH_TOKEN:
                return service.refreshToken(request);
            default:
                throw new UnsupportedOperationException("No other types are supported");
        }
    }
}
