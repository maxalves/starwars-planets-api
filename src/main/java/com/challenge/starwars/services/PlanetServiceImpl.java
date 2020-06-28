package com.challenge.starwars.services;

import com.challenge.starwars.application.exception.PlanetNotFoundException;
import com.challenge.starwars.exceptions.PlanetAlreadyExistsException;
import com.challenge.starwars.models.Planet;
import com.challenge.starwars.repositories.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PlanetServiceImpl implements PlanetService {
    private final PlanetRepository planetRepository;

    @Autowired
    public PlanetServiceImpl(PlanetRepository planetRepository) {
        this.planetRepository = planetRepository;
    }

    @Override
    public Planet create(Planet planet) {
        if (Objects.nonNull(planetRepository.findByName(planet.getName())))
            throw new PlanetAlreadyExistsException(planet);

        return planetRepository.save(planet);
    }

    @Override
    public Planet findById(String id) {
        var planet = planetRepository.findById(id);

        if (planet.isEmpty())
            throw new PlanetNotFoundException(id);

        return planet.get();
    }

    @Override
    public List<Planet> findAll(Page page) {
        return null;
    }

    @Override
    public List<Planet> findAllByName(Page page, String name) {
        return null;
    }

    @Override
    public Planet delete(String id) {
        return null;
    }
}
