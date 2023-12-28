package com.waspsecurity.waspsecurity.entities;


import com.waspsecurity.waspsecurity.FieldPropertyVisibilityStrategy;
import com.waspsecurity.waspsecurity.enums.Role;
import com.waspsecurity.waspsecurity.utils.Argon2Utils;
import jakarta.json.bind.annotation.JsonbVisibility;
import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonbVisibility(FieldPropertyVisibilityStrategy.class)


public class User implements Serializable {
    @Id
    @Column("email")
    private String email;
    @Column("firstName")
    private String firstName;
    @Column("lastName")
    private String lastName;
    @Column("addressId")
    private String address_id;
    @Column("phoneNumber")
    private String phoneNumber;
    @Column("password")
    private String password;
    @Column("userRole")
    private Set<Role> roles;
    @Column
    private boolean archived;
    @Column
    private String created_on;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress_id() {
        return address_id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getCreated_on() {
        return created_on;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public User(String email, String firstName, String lastName, String address_id, String phoneNumber, String password, Set<Role> roles, boolean archived) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address_id = address_id;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.roles = roles;
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, firstName, lastName, address_id, phoneNumber, password, roles, archived);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "email='" + email + '\'' +
                ", forename='" + firstName + '\'' +
                ", surname='" + lastName + '\'' +
                ", address_id='" + address_id + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", archived=" + archived +
                '}';
    }

    public void hashPassword(String password, Argon2Utils argon2Utility) {
        this.password = argon2Utility.hash(password.toCharArray());
    }
}
