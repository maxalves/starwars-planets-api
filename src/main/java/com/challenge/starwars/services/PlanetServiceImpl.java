package com.challenge.starwars.services;

import com.challenge.starwars.exceptions.PlanetAlreadyExistsException;
import com.challenge.starwars.exceptions.PlanetNotFoundException;
import com.challenge.starwars.models.Planet;
import com.challenge.starwars.repositories.PlanetRepository;
import com.challenge.starwars.services.external.StarWarsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class PlanetServiceImpl implements PlanetService {
    private static final String CACHE_NAME = "planets";

    private final PlanetRepository planetRepository;
    private final StarWarsService starWarsService;


    @CachePut(cacheNames = CACHE_NAME, key = "#planet.id",
            unless = "#result.filmApparitionsCount == null")
    @Override
    public Planet create(Planet planet) {
        planetRepository.findByName(planet.getName())
                .ifPresent(p -> { throw new PlanetAlreadyExistsException(p); });

        return setPlanetFilmApparitionsCountOn(planetRepository.save(planet));
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#id",
            unless = "#result.filmApparitionsCount == null")
    @Override
    public Planet findById(String id) {
        return planetRepository.findById(id)
                .map(this::setPlanetFilmApparitionsCountOn)
                .orElseThrow(() -> new PlanetNotFoundException(id));
    }

    @Override
    public Page<Planet> findAll(Pageable page) {
        var planets = planetRepository.findAll(page);

        planets.getContent().forEach(this::setPlanetFilmApparitionsCountOn);

        return planets;
    }

    @Override
    public Page<Planet> findAllByName(Pageable page, String name) {
        var planets = planetRepository.findAllByNameContainingIgnoreCase(page, name);

        planets.getContent().forEach(this::setPlanetFilmApparitionsCountOn);

        return planets;
    }

    @CacheEvict(cacheNames = CACHE_NAME, key = "#id")
    @Override
    public Planet delete(String id) {
        var planet = planetRepository.findById(id)
                .orElseThrow(() -> new PlanetNotFoundException(id));

        planetRepository.deleteById(id);

        return planet;
    }

    private Planet setPlanetFilmApparitionsCountOn(Planet planet) {
        starWarsService.getFilmsCount(planet.getName())
                .ifPresent(planet::setFilmApparitionsCount);

        return planet;
    }
}
