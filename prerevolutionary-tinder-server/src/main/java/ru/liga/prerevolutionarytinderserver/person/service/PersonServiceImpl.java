package ru.liga.prerevolutionarytinderserver.person.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.liga.prerevolutionarytinderserver.person.domain.Person;
import ru.liga.prerevolutionarytinderserver.person.exception.PersonNotFoundException;
import ru.liga.prerevolutionarytinderserver.person.repository.PersonRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    @Override
    public Page<Person> getAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    @Override
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public void updatePerson(Long personId, Person person) {
        Person existingPerson = getPersonById(personId);
        existingPerson = existingPerson.updateFromOther(person);
        personRepository.save(existingPerson);
    }

    @Override
    public void deletePerson(Long personId) {
        deleteReferencesToPerson(personId);
        personRepository.deleteById(personId);
    }

    private void deleteReferencesToPerson(Long personId) {
        personRepository.findById(personId).ifPresent(
                person -> {
                    person.getLikedBy().forEach(x -> x.removeLikedPerson(person));
                    personRepository.saveAll(person.getLikedBy());
                }
        );
    }

    @Override
    public void likePerson(Long sourcePersonId, Long targetPersonId) {
        Person sourcePerson = getPersonById(sourcePersonId);
        Person targetPerson = getPersonById(targetPersonId);
        sourcePerson.addLikedPerson(targetPerson);
        personRepository.save(sourcePerson);
    }

    @Override
    public void unlikePerson(Long sourcePersonId, Long targetPersonId) {
        Person sourcePerson = getPersonById(sourcePersonId);
        Person targetPerson = getPersonById(targetPersonId);
        sourcePerson.removeLikedPerson(targetPerson);
        personRepository.save(sourcePerson);
    }

    @Override
    public Page<Person> getPersonMatches(Long personId, Pageable pageable) {
        Person existingPerson = getPersonById(personId);
        List<Person> personMatches = existingPerson.findMatches().stream().toList();
        return buildPersonPage(pageable, personMatches);
    }

    @Override
    public Page<Person> getPersonsForPreference(Long personId, Pageable pageable) {
        List<Person> preferredPersons = findPersonsForPreference(getPersonById(personId));
        return buildPersonPage(pageable, preferredPersons);
    }

    private List<Person> findPersonsForPreference(Person person) {
        return personRepository.findByGenderIn(person.getPersonPreferences()).stream()
                .filter(x -> x.getPersonPreferences().contains(person.getGender()) && !x.equals(person))
                .toList();
    }

    private PageImpl<Person> buildPersonPage(Pageable pageable, List<Person> persons) {
        List<Person> personPageContent = persons.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
        return new PageImpl<>(personPageContent, pageable, persons.size());
    }


}
