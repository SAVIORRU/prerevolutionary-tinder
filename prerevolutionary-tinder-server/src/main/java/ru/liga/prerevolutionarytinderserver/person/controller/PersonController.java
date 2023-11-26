package ru.liga.prerevolutionarytinderserver.person.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.liga.prerevolutionarytinderserver.person.controller.dto.PersonDto;
import ru.liga.prerevolutionarytinderserver.person.controller.dto.PersonDtoConverter;
import ru.liga.prerevolutionarytinderserver.person.domain.Person;
import ru.liga.prerevolutionarytinderserver.person.service.PersonService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {
    private final PersonService personService;
    private final PersonDtoConverter dtoConverter;

    @PostMapping(value = "/{personId}")
    public ResponseEntity<?> createPerson(@PathVariable Long personId, @Valid @RequestBody PersonDto personDto) {
        Person person = dtoConverter.mapDtoToPerson(personDto);
        person.setId(personId);
        person = personService.createPerson(person);
        return ResponseEntity.ok(person.getId());
    }

    @GetMapping(value = "/{personId}")
    public ResponseEntity<?> getPersonById(@PathVariable Long personId) {
        return ResponseEntity.ok(dtoConverter.mapPersonToDto(personService.getPersonById(personId)));
    }

    @GetMapping
    public ResponseEntity<?> getAllPersons(Pageable pageable) {
        return ResponseEntity.ok(convertPersonPage(personService.getAllPersons(pageable)));
    }

    @GetMapping(value = "/{personId}/matches")
    public ResponseEntity<?> getPersonMatches(@PathVariable Long personId, Pageable pageable) {
        return ResponseEntity.ok(convertPersonPage(personService.getPersonMatches(personId, pageable)));
    }

    @GetMapping(value = "/{personId}/preferredPersons")
    public ResponseEntity<?> getPreferredPersons(@PathVariable Long personId, Pageable pageable) {
        return ResponseEntity.ok(convertPersonPage(personService.getPersonsForPreference(personId, pageable)));
    }

    @PutMapping(value = "/{personId}")
    public ResponseEntity<?> updatePerson(@PathVariable Long personId, @Valid @RequestBody PersonDto personDto) {
        Person person = dtoConverter.mapDtoToPerson(personDto);
        personService.updatePerson(personId, person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{personId}")
    public ResponseEntity<?> deletePerson(@PathVariable Long personId) {
        personService.deletePerson(personId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{sourcePersonId}/like")
    public ResponseEntity<?> likePerson(@PathVariable Long sourcePersonId,
                                        @RequestParam(name = "personId") Long targetPersonId) {
        personService.likePerson(sourcePersonId, targetPersonId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{sourcePersonId}/unlike")
    public ResponseEntity<?> unlikePerson(@PathVariable Long sourcePersonId,
                                          @RequestParam(name = "personId") Long targetPersonId) {
        personService.unlikePerson(sourcePersonId, targetPersonId);
        return ResponseEntity.ok().build();
    }

    private Page<PersonDto> convertPersonPage(Page<Person> personPage) {
        return new PageImpl<>(personPage.getContent().stream()
                .map(dtoConverter::mapPersonToDto)
                .toList(), personPage.getPageable(), personPage.getTotalElements());

    }
}
