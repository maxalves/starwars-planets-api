package com.challenge.starwars.services.external;

import com.challenge.starwars.models.swapi.SwapiPlanetResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SWAPIServiceImpl implements StarWarsService {

    private final SWAPIClient swapiClient;

    @Autowired
    public SWAPIServiceImpl(SWAPIClient swapiClient) {
        this.swapiClient = swapiClient;
    }

    @HystrixCommand(fallbackMethod = "getDefaultFilmsCount")
    public Integer getFilmsCount(String planetName) {
        var response = getPlanetInformation(planetName);

        if (response.getResults().isEmpty())
            return getDefaultFilmsCount(planetName);

        return response
                .getResults()
                .stream()
                .filter(planet -> planet.getName().equals(planetName))
                .mapToInt(planet -> planet.getFilms().size())
                .sum();
    }

    private Integer getDefaultFilmsCount(String planetName){
        return 0;
    }

    private SwapiPlanetResponse getPlanetInformation(String planetName) {
        return swapiClient.getPlanetsByName(planetName);
    }
}
