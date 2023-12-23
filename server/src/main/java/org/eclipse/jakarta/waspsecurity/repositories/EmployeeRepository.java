package org.eclipse.jakarta.waspsecurity.repositories;

import jakarta.nosql.mapping.Repository;
import org.eclipse.jakarta.waspsecurity.entities.Employee;

import java.util.Optional;
import java.util.stream.Stream;

public interface EmployeeRepository extends Repository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    Stream<Employee> findAll();
    Stream<Employee> findByArchived(boolean archived);
}
