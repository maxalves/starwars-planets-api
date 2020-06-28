package com.challenge.starwars.controllers;

import com.challenge.starwars.exceptions.PlanetNotFoundException;
import com.challenge.starwars.exceptions.PlanetAlreadyExistsException;
import com.challenge.starwars.models.CustomError;
import com.challenge.starwars.services.PlanetServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackageClasses = {PlanetController.class, PlanetServiceImpl.class})
public class PlanetControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PlanetAlreadyExistsException.class)
    public CustomError planetAlreadyExistsExceptionHandler(PlanetAlreadyExistsException error) {
        log.error("Planet already exists", error);
        return CustomError.builder().message(error.getMessage()).build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CustomError methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException error) {
        log.error("Planet is invalid", error);

        Map<String, String> errors = new HashMap<>();
        error.getBindingResult().getAllErrors().forEach((e) -> {
            String fieldName = ((FieldError) e).getField();
            String errorMessage = e.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return CustomError.builder().message("Planet is invalid").fields(errors).build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PlanetNotFoundException.class)
    public CustomError planetNotFoundExceptionHandler(PlanetNotFoundException error) {
        log.error("Planet not found", error);
        return CustomError.builder().message(error.getMessage()).build();
    }
}
