package ru.liga.prerevolutionarytinderserver.person.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.liga.prerevolutionarytinderserver.person.exception.PersonGenderConvertionException;

import java.util.Arrays;

@RequiredArgsConstructor
public enum PersonGender {
    MALE("male"),
    FEMALE("female");

    @Getter
    private final String genderText;

    public static PersonGender ofText(String genderText) {
        String preparedGenderText = genderText.trim().toLowerCase();
        return Arrays.stream(PersonGender.values())
                .filter(x -> x.genderText.equals(preparedGenderText))
                .findFirst().orElseThrow(() -> new PersonGenderConvertionException(genderText));
    }
}
