package ru.liga.prerevolutionarytinderserver.person.controller.dto;


import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.liga.prerevolutionarytinderserver.person.domain.Person;
import ru.liga.prerevolutionarytinderserver.person.enums.PersonGender;
import ru.liga.prerevolutionarytinderserver.person.service.PersonService;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersonDtoConverterImpl implements PersonDtoConverter {
    private final ModelMapper modelMapper;
    private final PersonService personService;

    public PersonDtoConverterImpl(@Autowired PersonService personService) {
        this.modelMapper = buildModelMapper();
        this.personService = personService;
    }

    private ModelMapper buildModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.createTypeMap(PersonDto.class, Person.class);
        mapper.createTypeMap(Person.class, PersonDto.class);
        addConverters(mapper);
        return mapper;
    }

    private void addConverters(ModelMapper mapper) {
        addPersonsToIdsConverter(mapper);
        addIdsToPersonsConverter(mapper);

        addStringToPersonGenderConverter(mapper);
        addPersonGenderToStringConverter(mapper);
        addStringsToPersonGendersConverter(mapper);
        addPersonGendersToStringsConverter(mapper);
    }

    private void addPersonGenderToStringConverter(ModelMapper mapper) {
        TypeMap<Person, PersonDto> personToDtoMap = mapper.getTypeMap(Person.class, PersonDto.class);
        Converter<PersonGender, String> personGenderToStringConverter = mappingContext ->
                mappingContext.getSource().getGenderText();
        personToDtoMap.addMappings(map -> map.using(personGenderToStringConverter)
                .map(Person::getGender, PersonDto::setGender));
    }

    private void addStringToPersonGenderConverter(ModelMapper mapper) {
        TypeMap<PersonDto, Person> dtoToPersonMap = mapper.getTypeMap(PersonDto.class, Person.class);
        Converter<String, PersonGender> stringToPersonGenderConverter = mappingContext ->
                PersonGender.ofText(mappingContext.getSource());
        dtoToPersonMap.addMappings(map -> map.using(stringToPersonGenderConverter)
                .map(PersonDto::getGender, Person::setGender));
    }

    private void addIdsToPersonsConverter(ModelMapper mapper) {
        Converter<Collection<Long>, Set<Person>> idsToPersonsConverter = mappingContext -> new HashSet<>();
        TypeMap<PersonDto, Person> dtoToPersonMap = mapper.getTypeMap(PersonDto.class, Person.class);
        dtoToPersonMap.addMappings(map -> map.using(idsToPersonsConverter)
                .map(PersonDto::getLikedPersons, Person::setLikedPersons));
        dtoToPersonMap.addMappings(map -> map.using(idsToPersonsConverter)
                .map(PersonDto::getLikedBy, Person::setLikedBy));
    }

    private void addPersonsToIdsConverter(ModelMapper mapper) {
        Converter<Collection<Person>, List<Long>> personsToIdsConverter = mappingContext -> {
            Collection<Person> persons = mappingContext.getSource();
            return persons.stream()
                    .map(Person::getId)
                    .toList();
        };
        TypeMap<Person, PersonDto> personToDtoMap = mapper.getTypeMap(Person.class, PersonDto.class);
        personToDtoMap.addMappings(map -> map.using(personsToIdsConverter)
                .map(Person::getLikedPersons, PersonDto::setLikedPersons));
        personToDtoMap.addMappings(map -> map.using(personsToIdsConverter)
                .map(Person::getLikedBy, PersonDto::setLikedBy));
    }

    private void addPersonGendersToStringsConverter(ModelMapper mapper) {
        TypeMap<Person, PersonDto> personToDtoMap = mapper.getTypeMap(Person.class, PersonDto.class);
        Converter<Collection<PersonGender>, List<String>> personGendersToStringsConverter =
                mappingContext -> mappingContext.getSource().stream()
                .map(PersonGender::getGenderText)
                .toList();
        personToDtoMap.addMappings(map -> map.using(personGendersToStringsConverter)
                .map(Person::getPersonPreferences, PersonDto::setPersonPreferences));
    }

    private void addStringsToPersonGendersConverter(ModelMapper mapper) {
        TypeMap<PersonDto, Person> dtoToPersonMap = mapper.getTypeMap(PersonDto.class, Person.class);
        Converter<Collection<String>, Set<PersonGender>> stringsToPersonGendersConverter =
                mappingContext -> mappingContext.getSource().stream()
                        .map(PersonGender::ofText)
                        .collect(Collectors.toSet());
        dtoToPersonMap.addMappings(map -> map.using(stringsToPersonGendersConverter)
                .map(PersonDto::getPersonPreferences, Person::setPersonPreferences));
    }

    @Override
    public Person mapDtoToPerson(PersonDto personDto) {
        Person person = modelMapper.map(personDto, Person.class);
        person.getLikedPersons().addAll(resolvePersonIds(personDto.getLikedPersons()));
        person.getLikedBy().addAll(resolvePersonIds(personDto.getLikedBy()));
        return person;
    }

    private Set<Person> resolvePersonIds(Collection<Long> personIds) {
        return personIds.stream()
                .map(personService::getPersonById)
                .collect(Collectors.toSet());
    }

    @Override
    public PersonDto mapPersonToDto(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }
}
