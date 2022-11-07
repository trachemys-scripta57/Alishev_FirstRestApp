package com.banana.FirstRestApp.services;

import com.banana.FirstRestApp.models.Person;
import com.banana.FirstRestApp.repositories.PeopleRepositories;
import com.banana.FirstRestApp.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepositories peopleRepositories;

    @Autowired
    public PeopleService(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }
    public List<Person> findAll() {
        return peopleRepositories.findAll();
    }
    public Person findOne(int id) {
        Optional<Person> foundPerson = peopleRepositories.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }
    @Transactional
    public void save(Person person) {
        enrichPerson(person);

        peopleRepositories.save(person);
    }

    private void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
    }
}
