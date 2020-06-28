package com.challenge.starwars.controllers;

import com.challenge.starwars.exceptions.PlanetAlreadyExistsException;
import com.challenge.starwars.models.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackageClasses = {PlanetController.class})
public class PlanetControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PlanetAlreadyExistsException.class)
    public CustomError planetAlreadyExistsExceptionHandler(PlanetAlreadyExistsException error) {
        return new CustomError();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException error) {
        Map<String, String> errors = new HashMap<>();
        error.getBindingResult().getAllErrors().forEach((e) -> {
            String fieldName = ((FieldError) e).getField();
            String errorMessage = e.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
