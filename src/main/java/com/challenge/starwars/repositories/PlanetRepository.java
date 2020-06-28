package com.challenge.starwars.repositories;

import com.challenge.starwars.models.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String> {
    Planet save(Planet planet);

    Optional<Planet> findById(String id);

    Planet findByName(String name);
}
