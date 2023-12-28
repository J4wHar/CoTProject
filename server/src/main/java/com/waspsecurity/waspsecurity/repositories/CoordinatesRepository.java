package com.waspsecurity.waspsecurity.repositories;

import com.waspsecurity.waspsecurity.entities.Coordinates;
import com.waspsecurity.waspsecurity.entities.User;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

import java.util.Optional;


@Repository
public interface CoordinatesRepository extends CrudRepository<Coordinates, String> {
    Optional<Coordinates> findByEmail(String email);
}
