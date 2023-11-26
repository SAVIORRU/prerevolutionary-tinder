package ru.liga.prerevolutionarytinderserver.person.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.liga.prerevolutionarytinderserver.person.controller.PersonController;
import ru.liga.prerevolutionarytinderserver.person.exception.PersonGenderConvertionException;
import ru.liga.prerevolutionarytinderserver.person.exception.PersonNotFoundException;

@ControllerAdvice(assignableTypes = {PersonController.class})
@Slf4j
public class PersonControllerAdvice {

    @ExceptionHandler(value = {PersonNotFoundException.class})
    public ResponseEntity<?> handlePersonNotFoundException(PersonNotFoundException exception) {
        log.debug("Request for non-existed Person entity id {}", exception.getPersonId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(value = {PersonGenderConvertionException.class})
    public ResponseEntity<?> handlePersonGenderConvertionException(PersonGenderConvertionException exception) {
        log.debug("Error during PersonGender convertion with text {}", exception.getInvalidPersonGenderText());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
