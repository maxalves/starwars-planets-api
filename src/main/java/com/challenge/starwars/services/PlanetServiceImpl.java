package com.challenge.starwars.services;

import com.challenge.starwars.exceptions.PlanetAlreadyExistsException;
import com.challenge.starwars.exceptions.PlanetNotFoundException;
import com.challenge.starwars.models.Planet;
import com.challenge.starwars.repositories.PlanetRepository;
import com.challenge.starwars.services.external.StarWarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PlanetServiceImpl implements PlanetService {
    private static final String CACHE_NAME = "planets";
    private final PlanetRepository planetRepository;
    private final StarWarsService starWarsService;

    @Autowired
    public PlanetServiceImpl(PlanetRepository planetRepository, StarWarsService starWarsService) {
        this.planetRepository = planetRepository;
        this.starWarsService = starWarsService;
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#id")
    @Override
    public Planet create(Planet planet) {
        if (Objects.nonNull(planetRepository.findByName(planet.getName())))
            throw new PlanetAlreadyExistsException(planet);

        planet = planetRepository.save(planet);
        setPlanetFilmApparitionsCountOn(planet);

        return planet;
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#id")
    @Override
    public Planet findById(String id) {
        var planet = planetRepository.findById(id);

        if (planet.isEmpty())
            throw new PlanetNotFoundException(id);

        starWarsService.getFilmsCount(planet.get().getName())
                .ifPresent(apparitions -> planet.get().setFilmApparitionsCount(apparitions));

        return planet.get();
    }

    @Override
    public Page<Planet> findAll(Pageable page) {
        var planets = planetRepository.findAll(page);

        planets.getContent().forEach(this::setPlanetFilmApparitionsCountOn);

        return planets;
    }

    @Override
    public Page<Planet> findAllByName(Pageable page, String name) {
        return planetRepository.findAllByNameContainingIgnoreCase(page, name);
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "#id")
    @Override
    public Planet delete(String id) {
        var planet = planetRepository.findById(id);

        if (planet.isEmpty())
            throw new PlanetNotFoundException(id);

        planetRepository.deleteById(id);

        return planet.get();
    }

    private void setPlanetFilmApparitionsCountOn(Planet planet) {
        starWarsService.getFilmsCount(planet.getName())
                .ifPresent(planet::setFilmApparitionsCount);
    }
}
