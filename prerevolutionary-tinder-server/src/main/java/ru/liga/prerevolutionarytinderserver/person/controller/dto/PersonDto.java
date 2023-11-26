package ru.liga.prerevolutionarytinderserver.person.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
public class PersonDto {

    private Long id;

    @EqualsAndHashCode.Exclude
    @NotBlank
    private String name;

    @NotBlank
    @EqualsAndHashCode.Exclude
    private String gender;

    @EqualsAndHashCode.Exclude
    private String description;

    @NotNull
    @EqualsAndHashCode.Exclude
    private List<Long> likedPersons;

    @NotNull
    @EqualsAndHashCode.Exclude
    private List<Long> likedBy;

    @NotNull
    @EqualsAndHashCode.Exclude
    private List<String> personPreferences;
}
