package com.waspsecurity.waspsecurity.security;


import com.waspsecurity.waspsecurity.Exceptions.EmployeeNotAuthorizedException;
import com.waspsecurity.waspsecurity.entities.*;
import com.waspsecurity.waspsecurity.models.Oauth2Request;
import com.waspsecurity.waspsecurity.models.Oauth2Response;
import com.waspsecurity.waspsecurity.models.Token;
import com.waspsecurity.waspsecurity.models.UserJWT;
import com.waspsecurity.waspsecurity.repositories.UserTokenRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;


@ApplicationScoped
public class Oauth2Service {
    private static final Config config = ConfigProvider.getConfig();

    static final int EXPIRE_IN = config.getValue("jwt.lifetime.duration",Integer.class);
    static final Duration EXPIRES = Duration.ofSeconds(EXPIRE_IN);
    @Inject
    UserTokenRepository userTokenRepository ;

    @Inject
    SecurityService securityService;

    @Inject
    Validator validator;

    public Oauth2Response token(Oauth2Request request) {

        final User user = securityService.findBy(request.getEmail(), request.getPassword());

        Optional<UserToken> optionalUserToken = userTokenRepository.findByEmail(request.getEmail()) ;
        UserToken userToken ;
        if(optionalUserToken.isPresent()){
            userToken=optionalUserToken.get() ;
        }else{
            userToken=new UserToken(request.getEmail()) ;
        }

        final Token token= Token.generate() ;
        final String jwt = UserJWT.createToken(user, token, EXPIRES);
        AccessToken accessToken = new AccessToken(token.get(), jwt, EXPIRES);
        //here the error
        RefreshToken refreshToken = new RefreshToken(Token.generate(), accessToken);
        userToken.add(refreshToken);
        userTokenRepository.save(userToken);
        final Oauth2Response response = Oauth2Response.of(accessToken, refreshToken, EXPIRE_IN, user.getEmail(), user.getRoles());
        return response;
    }

    public Oauth2Response refreshToken(Oauth2Request request) {
        System.out.println("refresh methode is activated");
        final Set<ConstraintViolation<Oauth2Request>> violations = validator.validate(request, Oauth2Request.RefreshToken.class);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        System.out.println(userTokenRepository.findByRefreshToken(request.getRefreshToken()));
        final UserToken userToken = userTokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new EmployeeNotAuthorizedException("Unauthorized"));

        final User user = securityService.findBy(userToken.getEmail());
        final String employeeId = user.getEmail();
        final Token token = Token.generate();
        final String jwt = UserJWT.createToken(user, token, EXPIRES);
        AccessToken accessToken = new AccessToken(token.get(), jwt, EXPIRES);
        RefreshToken refreshToken = userToken.update(accessToken, request.getRefreshToken(), userTokenRepository);
        final Oauth2Response response = Oauth2Response.of(accessToken, refreshToken, EXPIRE_IN, employeeId, user.getRoles());

        return response;
    }

}
