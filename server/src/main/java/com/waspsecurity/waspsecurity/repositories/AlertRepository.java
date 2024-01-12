package com.waspsecurity.waspsecurity.repositories;

import com.waspsecurity.waspsecurity.entities.Alert;
import com.waspsecurity.waspsecurity.entities.Coordinates;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

import java.util.Optional;

@Repository
public interface AlertRepository extends CrudRepository<Alert, String> {
    Optional<Alert> findById(Long id);
}
