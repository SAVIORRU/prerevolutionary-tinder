package ru.liga.prerevolutionarytinderserver.person.controller.dto;

import ru.liga.prerevolutionarytinderserver.person.domain.Person;

public interface PersonDtoConverter {
    Person mapDtoToPerson(PersonDto personDto);

    PersonDto mapPersonToDto(Person person);
}
