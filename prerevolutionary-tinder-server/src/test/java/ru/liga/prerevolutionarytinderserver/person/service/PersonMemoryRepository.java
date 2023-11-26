package ru.liga.prerevolutionarytinderserver.person.service;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import ru.liga.prerevolutionarytinderserver.person.domain.Person;
import ru.liga.prerevolutionarytinderserver.person.enums.PersonGender;
import ru.liga.prerevolutionarytinderserver.person.repository.PersonRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PersonMemoryRepository implements PersonRepository {

    private final List<Person> persons = new ArrayList<>();
    private Long currentId = 0L;

    @Override
    public void flush() {

    }

    @Override
    public <S extends Person> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Person> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Person> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Person getOne(Long aLong) {
        return null;
    }

    @Override
    public Person getById(Long aLong) {
        return null;
    }

    @Override
    public Person getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Person> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Person> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Person> List<S> saveAll(Iterable<S> entities) {
        List<S> persons = new ArrayList<>();
        while (entities.iterator().hasNext()) {
            Person person = entities.iterator().next();
            person = save(person);
            persons.add((S) person);
        }
        return persons;
    }

    @Override
    public List<Person> findAll() {
        return null;
    }

    @Override
    public List<Person> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public <S extends Person> S save(S entity) {
        Optional<Person> existingPerson = findById(entity.getId());
        Person person = null;
        if (existingPerson.isPresent()){
            person = existingPerson.get();
            person.updateFromOther(entity);
        } else {
            entity.setId(getNextId());
            persons.add(entity);
            person = entity;
        }
        return (S) person;
    }

    @Override
    public Optional<Person> findById(Long aLong) {
        Optional<Person> person = persons.stream()
                .filter(x -> x.getId().equals(aLong))
                .findFirst();
        person.ifPresent(x -> x.setLikedBy(getLikedForPerson(x)));
        return person;
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {
        findById(aLong).ifPresent(this::delete);
    }

    @Override
    public void delete(Person entity) {
        persons.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Person> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Person> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        List<Person> pageContent = this.persons.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();

        return new PageImpl<>(pageContent, pageable, this.persons.size());
    }

    @Override
    public <S extends Person> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Person> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Person> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Person> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Person, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public Set<Person> findByGenderIn(Set<PersonGender> gendersForSearch) {
        return persons.stream()
                .filter(x -> gendersForSearch.contains(x.getGender()))
                .collect(Collectors.toSet());
    }

    private Long getNextId() {
        Long newId = currentId;
        currentId++;
        return newId;
    }

    private Set<Person> getLikedForPerson(Person person) {
        return persons.stream()
                .flatMap(x -> x.getLikedPersons().stream())
                .filter(x -> x.getId().equals(person.getId()))
                .collect(Collectors.toSet());
    }
}
