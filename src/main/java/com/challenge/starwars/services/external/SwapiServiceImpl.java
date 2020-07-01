package com.challenge.starwars.services.external;

import com.challenge.starwars.models.external.SwapiPlanet;
import com.challenge.starwars.models.external.SwapiPlanetResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SwapiServiceImpl implements StarWarsService {

    private final SwapiClient swapiClient;

    @Autowired
    public SwapiServiceImpl(SwapiClient swapiClient) {
        this.swapiClient = swapiClient;
    }

    @HystrixCommand(fallbackMethod = "getFallbackFilmsCount")
    public Optional<Integer> getFilmsCount(String planetName) {
        var filmCount = findPlanet(planetName)
                .map(planet -> planet.getFilms().size())
                .orElse(0);

        return Optional.of(filmCount);
    }

    private Optional<Integer> getFallbackFilmsCount(String planetName) {
        return Optional.empty();
    }

    private Optional<SwapiPlanet> findPlanet(String planetName) {
        return getSwapiPlanetResponse(planetName).getResults()
                .stream()
                .filter(planet -> planet.getName().equals(planetName))
                .findFirst();
    }

    private SwapiPlanetResponse getSwapiPlanetResponse(String planetName) {
        return swapiClient.getPlanetsByName(planetName);
    }
}
