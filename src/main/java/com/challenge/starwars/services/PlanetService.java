package com.challenge.starwars.services;

import com.challenge.starwars.models.Planet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlanetService {
    Planet create(Planet planet);

    Planet findById(String id);

    Page<Planet> findAll(Pageable page);

    Page<Planet> findAllByName(Pageable page, String name);

    Planet delete(String id);
}
