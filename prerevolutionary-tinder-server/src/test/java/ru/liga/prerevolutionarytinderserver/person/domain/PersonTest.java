package ru.liga.prerevolutionarytinderserver.person.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;


public class PersonTest {
    @Test
    public void given_twoPersonsWithMatch_findMatches_output_oneMatchedPerson() {
        Person firstPerson = Person.builder()
                .id(0L)
                .likedPersons(new HashSet<>())
                .likedBy(new HashSet<>())
                .build();
        Person secondPerson = Person.builder()
                .id(1L)
                .likedPersons(new HashSet<>())
                .likedBy(new HashSet<>())
                .build();
        firstPerson.addLikedPerson(secondPerson);
        firstPerson.getLikedBy().add(secondPerson);
        secondPerson.addLikedPerson(firstPerson);
        secondPerson.getLikedBy().add(firstPerson);
        Set<Person> personMatches = firstPerson.findMatches();
        assertThat(secondPerson).isIn(personMatches);
    }

    @Test
    public void given_twoPersonsWithOneLike_findMatches_output_noneMatches() {
        Person firstPerson = Person.builder()
                .id(0L)
                .likedPersons(new HashSet<>())
                .likedBy(new HashSet<>())
                .build();
        Person secondPerson = Person.builder()
                .id(1L)
                .likedPersons(new HashSet<>())
                .likedBy(new HashSet<>())
                .build();
        secondPerson.addLikedPerson(firstPerson);
        firstPerson.getLikedBy().add(secondPerson);
        Set<Person> personMatches = firstPerson.findMatches();
        assertThat(secondPerson).isNotIn(personMatches);
    }

}
