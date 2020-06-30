package com.challenge.starwars.repositories;

import com.challenge.starwars.models.Planet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String> {
    Optional<Planet> findById(String id);

    Page<Planet> findAll(Pageable pageable);

    Page<Planet> findAllByNameContainingIgnoreCase(Pageable pageable, String name);

    Optional<Planet> findByName(String name);
}
