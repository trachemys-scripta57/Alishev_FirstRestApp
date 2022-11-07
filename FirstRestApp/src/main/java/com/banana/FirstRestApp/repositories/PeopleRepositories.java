package com.banana.FirstRestApp.repositories;

import com.banana.FirstRestApp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepositories extends JpaRepository<Person, Integer> {
}
