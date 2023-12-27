package com.waspsecurity.waspsecurity.repositories;

import jakarta.data.repository.CrudRepository;
import com.waspsecurity.waspsecurity.entities.Employee;
import jakarta.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    Stream<Employee> findAll();
    Stream<Employee> findByArchived(boolean archived);
}
