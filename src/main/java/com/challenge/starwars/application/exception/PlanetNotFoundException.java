package com.challenge.starwars.application.exception;

public class PlanetNotFoundException extends RuntimeException {
    public PlanetNotFoundException(String id) {
        super(String.format("No planet found with id %s", id));
    }
}
