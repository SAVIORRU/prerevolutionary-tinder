package ru.liga.prerevolutionarytinderserver.person.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.liga.prerevolutionarytinderserver.person.domain.Person;

public interface PersonService {

    Person getPersonById(Long personId);

    Page<Person> getAllPersons(Pageable pageable);

    Page<Person> getPersonMatches(Long personId, Pageable pageable);

    Page<Person> getPersonsForPreference(Long personId, Pageable pageable);

    Person createPerson(Person person);

    void updatePerson(Long personId, Person person);

    void deletePerson(Long personId);

    void likePerson(Long sourcePersonId, Long targetPersonId);

    void unlikePerson(Long sourcePersonId, Long targetPersonId);
}
