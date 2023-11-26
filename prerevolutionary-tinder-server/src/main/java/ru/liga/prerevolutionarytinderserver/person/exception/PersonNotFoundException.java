package ru.liga.prerevolutionarytinderserver.person.exception;

import lombok.Getter;

public class PersonNotFoundException extends RuntimeException {

    @Getter
    private final Long personId;

    public PersonNotFoundException(Long id) {
        super(String.format("The person entity with id %s not found", id));
        this.personId = id;
    }
}
