package com.challenge.starwars.repositories;

import com.challenge.starwars.models.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String> {
    Planet save(Planet planet);

    Planet findByName(String name);
}
