package com.waspsecurity.waspsecurity.filters;


import com.waspsecurity.waspsecurity.repositories.UserTokenRepository;
import com.waspsecurity.waspsecurity.security.AccessToken;
import com.waspsecurity.waspsecurity.security.UserJWT;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Provider
@Priority(Priorities.AUTHENTICATION)
@Secured

public class AuthenticationFilter implements HttpAuthenticationMechanism {
    private static final Pattern CHALLENGE_PATTERN = Pattern.compile("^Bearer *([^ ]+) *$", Pattern.CASE_INSENSITIVE);
    private  static  final  List NOT_SECURED_PREFIX=List.of("oauth2","signup") ;

    @Inject
    UserTokenRepository repository;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
                                                HttpMessageContext httpMessageContext) {

        if(!(request.getRequestURI().contains("oauth2") || request.getRequestURI().contains("signup"))) {
            final String authorization = request.getHeader("Authorization");
            Matcher matcher = CHALLENGE_PATTERN.matcher(Optional.ofNullable(authorization).orElse(""));
            if (!matcher.matches()) {
                System.out.println(matcher);
                return httpMessageContext.responseUnauthorized();
            }
            final String token = matcher.group(1);

            final Optional<AccessToken> optional = repository.findByAccessToken(token)
                    .flatMap(u -> u.findAccessToken(token));

            final AccessToken accessToken = optional.get();
            final Optional<UserJWT> optionalUserJWT = UserJWT.parse(accessToken.getToken(), accessToken.getJwtSecret());
            if (optionalUserJWT.isPresent()) {
                final UserJWT userJWT = optionalUserJWT.get();
                return httpMessageContext.notifyContainerAboutLogin(userJWT.getUser(), userJWT.getRoles());
            } else {
                return httpMessageContext.responseUnauthorized();
            }

        }else {
            return  httpMessageContext.doNothing() ;
        }
    }

}
