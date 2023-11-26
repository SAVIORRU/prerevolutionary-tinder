package ru.liga.prerevolutionarytinderserver.person.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.liga.prerevolutionarytinderserver.person.enums.PersonGender;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "persons")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Person {

    @Id
    private Long id;

    private String name;

    private PersonGender gender;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinTable(
            name = "likes",
            joinColumns = {@JoinColumn(name = "liked_by",
                    nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "liked_person",
                    nullable = false, updatable = false)}
            ,
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT)
    )
    @Builder.Default
    private Set<Person> likedPersons = new HashSet<>();

    @ManyToMany(mappedBy = "likedPersons", fetch = FetchType.LAZY, cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @Builder.Default
    private Set<Person> likedBy = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "person_search_preferences",
            joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "preference_gender")
    @Builder.Default
    private Set<PersonGender> personPreferences = new HashSet<>();

    public Person updateFromOther(Person otherObject) {
        this.name = otherObject.getName();
        this.description = otherObject.getDescription();
        this.gender = otherObject.getGender();
        this.likedBy.clear();
        this.likedPersons.clear();
        this.likedBy.addAll(otherObject.likedBy);
        this.likedPersons.addAll(otherObject.likedPersons);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Person person = (Person) o;
        return getId() != null && Objects.equals(getId(), person.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addLikedPerson(Person person) {
        if (!this.equals(person)) {
            this.likedPersons.add(person);
        }
    }

    public void removeLikedPerson(Person person) {
        this.likedPersons.remove(person);
    }

    public Set<Person> findMatches() {
        return this.likedPersons.stream()
                .filter(this.likedBy::contains)
                .collect(Collectors.toSet());
    }
}
