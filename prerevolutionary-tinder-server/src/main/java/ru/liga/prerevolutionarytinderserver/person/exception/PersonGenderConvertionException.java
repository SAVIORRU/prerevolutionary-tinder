package ru.liga.prerevolutionarytinderserver.person.exception;

import lombok.Getter;

public class PersonGenderConvertionException extends RuntimeException {
    @Getter
    private final String invalidPersonGenderText;

    public PersonGenderConvertionException(String invalidPersonGenderText) {
        super(String.format
                ("Error during person gender convertion, got %s, accepted male, female (case insensitive)",
                        invalidPersonGenderText));
        this.invalidPersonGenderText = invalidPersonGenderText;
    }
}
