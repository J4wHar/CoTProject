package com.waspsecurity.waspsecurity.Resources;

import com.waspsecurity.waspsecurity.entities.Person;
import com.waspsecurity.waspsecurity.repositories.PersonRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/hello-world")
public class HelloResource {
    @Inject
    PersonRepository repository;
    @GET
    @Produces("text/plain")
    public String hello() {

        Person person = new Person();
        person.setId("123456");  // Set the ID
        person.setName("John Doe");
        var s = repository.save(person);
// Save an entity
        var x = repository.findByName("John Doe");

        return "Hello, World!";
    }
}