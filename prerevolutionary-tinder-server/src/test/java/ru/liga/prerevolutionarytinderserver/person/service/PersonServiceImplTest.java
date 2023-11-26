package ru.liga.prerevolutionarytinderserver.person.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.prerevolutionarytinderserver.person.domain.Person;
import ru.liga.prerevolutionarytinderserver.person.enums.PersonGender;
import ru.liga.prerevolutionarytinderserver.person.exception.PersonNotFoundException;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.*;

public class PersonServiceImplTest {
    private PersonService personService;

    @BeforeEach
    public void createService(){
        this.personService = new PersonServiceImpl(new PersonMemoryRepository());
    }

    @Test
    public void given_nonExistingPersonId_getPersonById_throws_PersonNotFoundException() {
        assertThatThrownBy(() -> personService.getPersonById(1L))
                .isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    public void given_NewPersonObject_createPerson_returns_PersonWithId(){
        Person person = new Person();
        person = personService.createPerson(person);
        assertThat(person.getId()).isEqualTo(0L);
    }

    @Test
    public void given_existingPersonId_getPersonById_returns_Person() {
        Person person = new Person();
        person = personService.createPerson(person);
        person = personService.getPersonById(0L);
        assertThat(person.getId()).isEqualTo(0L);
    }

    @Test
    public void given_personForUpdate_updatePerson_getUpdatedPerson() {
        Person person = new Person();
        person = personService.createPerson(person);
        assertThat(person.getName()).isNull();
        person = Person.builder().id(0L)
                .gender(PersonGender.MALE)
                .name("fooguy")
                .likedPersons(new HashSet<>())
                .likedBy(new HashSet<>())
                .build();
        personService.updatePerson(0L, person);
        person = personService.getPersonById(0L);
        assertThat(person.getName()).isEqualTo("fooguy");
        assertThat(person.getGender()).isEqualTo(PersonGender.MALE);
    }

    @Test
    public void given_existingPersonId_deletePerson_throws_exceptionAfterDelete_whenGetById(){
        Person person = new Person();
        person = personService.createPerson(person);
        personService.deletePerson(0L);
        assertThatThrownBy(() -> personService.getPersonById(0L))
                .isInstanceOf(PersonNotFoundException.class);
    }


}
