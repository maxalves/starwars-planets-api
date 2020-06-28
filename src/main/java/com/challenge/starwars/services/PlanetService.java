package com.challenge.starwars.services;

import com.challenge.starwars.models.Planet;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PlanetService {
    Planet create(Planet planet);

    Planet findById(String id);

    List<Planet> findAll(Page page);

    List<Planet> findAllByName(Page page, String name);

    Planet delete(String id);
}
