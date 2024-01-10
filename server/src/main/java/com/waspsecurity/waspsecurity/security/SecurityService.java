package com.waspsecurity.waspsecurity.security;


import com.waspsecurity.waspsecurity.Exceptions.EmployeeNotAuthorizedException;
import com.waspsecurity.waspsecurity.entities.User;
import com.waspsecurity.waspsecurity.enums.Role;
import com.waspsecurity.waspsecurity.repositories.UserRepository;
import com.waspsecurity.waspsecurity.utils.Argon2Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
@ApplicationScoped
public class SecurityService {

    @Inject
    UserRepository userRepository;
    @Inject
    Argon2Utils argon2Utils;

    public User findBy(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotAuthorizedException("Unauthorized"));
    }

    public User findBy(String email, String password) {
        final User user = userRepository.findById(email)
                .orElseThrow(() -> new EmployeeNotAuthorizedException("Unauthorized"));
        if (argon2Utils.check(user.getPassword() ,password.toCharArray() )) {
            return user;
        }
        throw new EmployeeNotAuthorizedException("Unauthorized");
    }
}
