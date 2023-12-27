package com.waspsecurity.waspsecurity.repositories;


import com.waspsecurity.waspsecurity.entities.Person;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, String> {
    List<Person> findByName(String name);

}


