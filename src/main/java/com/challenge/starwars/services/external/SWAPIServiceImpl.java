package com.challenge.starwars.services.external;

import com.challenge.starwars.models.external.SwapiPlanetResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SWAPIServiceImpl implements StarWarsService {

    private final SWAPIClient swapiClient;

    @Autowired
    public SWAPIServiceImpl(SWAPIClient swapiClient) {
        this.swapiClient = swapiClient;
    }

    @HystrixCommand(fallbackMethod = "getFallbackFilmsCount")
    public Optional<Integer> getFilmsCount(String planetName) {
        var response = getPlanetInformation(planetName);

        if (response.getResults().isEmpty())
            return Optional.of(0);

        var apparitionsCount = response
                .getResults()
                .stream().filter(planet -> planet.getName().equals(planetName))
                .mapToInt(planet -> planet.getFilms().size())
                .sum();

        return Optional.of(apparitionsCount);
    }

    private Optional<Integer> getFallbackFilmsCount(String planetName) {
        return Optional.empty();
    }

    private SwapiPlanetResponse getPlanetInformation(String planetName) {
        return swapiClient.getPlanetsByName(planetName);
    }
}
