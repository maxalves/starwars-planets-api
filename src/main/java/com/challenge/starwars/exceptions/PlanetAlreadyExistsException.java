package com.challenge.starwars.exceptions;

import com.challenge.starwars.models.Planet;

public class PlanetAlreadyExistsException extends RuntimeException {
    public PlanetAlreadyExistsException(Planet planet) {
        super(String.format("Already exists planet %s", planet.getName()));
    }
}
