package com.challenge.starwars.services;

import com.challenge.starwars.exceptions.PlanetNotFoundException;
import com.challenge.starwars.exceptions.PlanetAlreadyExistsException;
import com.challenge.starwars.models.Planet;
import com.challenge.starwars.repositories.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<Planet> findAll(Pageable page) {
        return planetRepository.findAll(page);
    }

    @Override
    public Page<Planet> findAllByName(Pageable page, String name) {
        return planetRepository.findAllByNameContainingIgnoreCase(page, name);
    }

    @Override
    public Planet delete(String id) {
        var planet = findById(id);

        planetRepository.deleteById(id);

        return planet;
    }
}
