package com.waspsecurity.waspsecurity.repositories;

import jakarta.data.repository.CrudRepository;
import com.waspsecurity.waspsecurity.entities.User;
import jakarta.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;
@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByEmail(String email);
    Stream<User> findAll();
    Stream<User> findByArchived(boolean archived);
}
