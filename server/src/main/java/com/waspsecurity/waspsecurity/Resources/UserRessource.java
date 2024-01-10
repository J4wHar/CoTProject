package com.waspsecurity.waspsecurity.Resources;


import com.waspsecurity.waspsecurity.Exceptions.EmployeeAlreadyExistsException;
import com.waspsecurity.waspsecurity.Exceptions.EmployeeNotFoundException;
import com.waspsecurity.waspsecurity.entities.User;
import com.waspsecurity.waspsecurity.enums.Role;
import com.waspsecurity.waspsecurity.models.Email;
import com.waspsecurity.waspsecurity.models.PasswordUpdate;
import com.waspsecurity.waspsecurity.filters.Secured;
import com.waspsecurity.waspsecurity.repositories.UserRepository;
import com.waspsecurity.waspsecurity.utils.Argon2Utils;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserRessource {
    private static final Supplier<WebApplicationException> NOT_FOUND =
            () -> new WebApplicationException(Response.Status.NOT_FOUND);
    @Inject
    UserRepository userRepository;
    @Inject
    Argon2Utils argon2Utils;
    @Secured
    @GET
    @RolesAllowed("ADMIN")
    public List<User> findActiveEmployees() {
        return userRepository.findByArchived(false).collect(Collectors.toList());
    }
    @POST
    @Path("/signup")
    public Response createEmployee(@Valid User user) {
        try {
            // we need to check if the user already exists !!!!
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new EmployeeAlreadyExistsException("Employee with id " + user.getEmail() + " already exists");
            }
            user.hashPassword(user.getPassword(), argon2Utils);
            user.setCreated_on(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a")));
            userRepository.save(user);
            return Response.ok("Employee added successfully!").build();
        }
        catch (EmployeeAlreadyExistsException e) {
            return  Response.status(400).entity(e.getMessage()).build();
        }
    }
    @POST
    @Secured
    @RolesAllowed("ADMIN")
    @Path("/add-user")
    public Response addEmployee(@Valid User user) {
        try {
            if (userRepository.findById(user.getEmail()).isPresent()) {
                throw new EmployeeNotFoundException("Employee with id " + user.getEmail() + " already exist!");
            }
            user.hashPassword(user.getPassword(), argon2Utils);
            user.setCreated_on(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy hh:mm:ss a")));
            userRepository.save(user);
            return Response.ok("Employee added successfully!").build();
        }
        catch (EmployeeNotFoundException e) {
            return Response.status(400, e.getMessage()).build();
        }
    }
     //We need to add update password (oldPass, newPass)
    @GET
    @Secured
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/{email}")
    public User getEmployeeById(@PathParam("email") String email) {
        return userRepository.findById(email).orElseThrow(NOT_FOUND);
    }
    @PUT
    @Secured
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/{email}")
    public Response updateEmployeeById(@PathParam("email") String email, User user) {
        try {
            if (!userRepository.findById(email).isPresent()){
                throw new EmployeeNotFoundException("Employee with email " + email + " NOT FOUND!");
            }
            User userInDb = userRepository.findById(email).get();
            if (userInDb.getFirstName() != user.getFirstName()) {
                userInDb.setFirstName(user.getFirstName());
            }
            if (userInDb.getLastName() != user.getLastName()) {
                userInDb.setLastName(user.getLastName());
            }
            if (userInDb.getRoles() != user.getRoles()) {
                userInDb.setRoles(user.getRoles());
            }
            if (userInDb.getAddress_id() != user.getAddress_id()) {
                userInDb.setRoles(user.getRoles());
            }
            if (userInDb.getPhoneNumber() != user.getPhoneNumber()) {
                userInDb.setPhoneNumber(user.getPhoneNumber());
            }
            userRepository.save(userInDb);
            return Response.ok("Employee with id " + email + " updated successfully!").build();
        }
        catch (EmployeeNotFoundException e) {
            return Response.status(400, e.getMessage()).build();
        }
    }
    @PATCH
    @Secured
    @RolesAllowed({"ADMIN", "USER"})
    @Path("/update-password/{email}")
    public Response updatePassword(@PathParam("email") String email, PasswordUpdate passwordUpdate) {
        try {
            if (!userRepository.findById(email).isPresent()) {
                throw new EmployeeNotFoundException("Employee with email " + email + " NOT FOUND!");
            }
            User userInDb = userRepository.findById(email).get();
            if (!argon2Utils.check(userInDb.getPassword(), passwordUpdate.getOldPassword().toCharArray())) {
                return Response.status(400, "Passwords Doesn't match").build();
            }
            userInDb.setPassword(passwordUpdate.getNewPassword());
            userInDb.hashPassword(userInDb.getPassword(), argon2Utils);
            userRepository.save(userInDb);
            return Response.ok("Password updated successfully").build();
        }
        catch (EmployeeNotFoundException e) {
            return Response.status(400, "Employee with email " + email + " NOT FOUND!").build();
        }
    }
    @DELETE
    @Secured
    @RolesAllowed("ADMIN")
    @Path("/{email}")
    public Response deleteEmployee(@PathParam("email") String email) {
        try {
            if (!userRepository.findById(email).isPresent()) {
                throw new EmployeeNotFoundException("Employee with email " + email + " NOT FOUND!");
            }
            User userInDb = userRepository.findById(email).get();
            userInDb.setArchived(true);
            userRepository.save(userInDb);
            return Response.ok("Employee with email " + email + " archived!").build();
        }
        catch (EmployeeNotFoundException e){
            return Response.status(400, e.getMessage()).build();
        }
    }
    @PATCH
    @Secured
    @RolesAllowed({"ADMIN"})
    @Path("/add-admin")
    public Response addAdmin(Email email) {
        try {
            User userInDb = userRepository.findByEmail(email.getEmail()).get();
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ADMIN);
            roles.add(Role.USER);
            userInDb.setRoles(roles);
            userRepository.save(userInDb);
            return Response.ok("user updated as admin successfully").build();
        }
        catch (Exception ex){
            return Response.status(400, ex.getMessage()).build();
        }
    }
}
