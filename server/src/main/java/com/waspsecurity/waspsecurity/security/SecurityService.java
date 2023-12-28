package com.waspsecurity.waspsecurity.security;


import com.waspsecurity.waspsecurity.Exceptions.EmployeeNotAuthorizedException;
import com.waspsecurity.waspsecurity.entities.User;
import com.waspsecurity.waspsecurity.enums.Role;
import com.waspsecurity.waspsecurity.repositories.EmployeeRepository;
import com.waspsecurity.waspsecurity.utils.Argon2Utils;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
@ApplicationScoped
public class SecurityService {

    @Inject
    EmployeeRepository employeeRepository;
    @Inject
    Argon2Utils argon2Utils;
    @Inject
    SecurityContext securityContext;

    /*
    public void create(Employee employee) {
        if (employeeRepository.findById(employee.getEmail()).isPresent()) {
            throw new EmployeeNotFoundException("Employee with email  " + employee.getEmail());
        } else {
            employee.hashPassword(employee.getPassword(), argon2Utils);
            employeeRepository.save(employee);
        }
    }

    public void delete(String email) {
        employeeRepository.deleteById(email);
    }

    public void updatePassword(String email, Employee dto) {
        final Principal principal = securityContext.getUserPrincipal();
        if (isForbidden(email, securityContext, principal)) {
            throw new EmployeeForbiddenException("Forbidden");
        }
        final Employee employee = employeeRepository.findById(email)
                .orElseThrow(() -> new EmployeeNotFoundException(email));
        employee.hashPassword(dto.getPassword(), argon2Utils);
        employeeRepository.save(employee);

    }

    public Employee getUser() {
        final Principal principal = securityContext.getUserPrincipal();
        if (principal == null) {
            throw new EmployeeNotAuthorizedToCreateAccountException("Employee NOT AUTHORIZED!");
        }
        final Employee employee = employeeRepository.findById(principal.getName())
                .orElseThrow(() -> new EmployeeNotFoundException(principal.getName()));
        return employee;
    }

    public void addRole(String email, Role role) {
        final Employee employee = employeeRepository.findById(email)
                .orElseThrow(() -> new EmployeeNotFoundException(email));

        Set<Role> roles = employee.getRoles() ;
        roles.add(role) ;
        employee.setRoles(roles);
        employeeRepository.save(employee);
    }

    public void removeRole(String  email , Role role) {
        final Employee user = employeeRepository.findById(email)

                .orElseThrow(() -> new EmployeeAlreadyExistsException(email));

        Set<Role> roles=user.getRoles() ;
        roles.remove(role) ;
        user.setRoles(roles);
        employeeRepository.save(user);
    }

    public List<Employee> getUsers() {
        return employeeRepository.findAll().collect(Collectors.toList());
    }
    */
    private boolean isForbidden(String  email, SecurityContext context, Principal principal) {
        return !(context.isUserInRole(Role.ADMIN.name()));

    }

    public User findBy(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EmployeeNotAuthorizedException("Unauthorized"));
    }

    public User findBy(String email, String password) {
        System.out.println("------------------------------------------------");
        System.out.println(employeeRepository.findById(email).toString());
        final User user = employeeRepository.findById(email)
                .orElseThrow(() -> new EmployeeNotAuthorizedException("Unauthorized"));
        System.out.println(argon2Utils.check(user.getPassword() ,password.toCharArray()));
        if (argon2Utils.check(user.getPassword() ,password.toCharArray() )) {

            return user;
        }
        throw new EmployeeNotAuthorizedException("Unauthorized");

    }


}
