package com.challenge.starwars.services.external;

import com.challenge.starwars.models.swapi.SwapiPlanetResponse;
import com.challenge.starwars.configuration.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "swapi",
        url = "${swapi.url}",
        configuration= FeignConfig.class)
public interface SWAPIClient {

    @GetMapping("/planets/?search={name}")
    SwapiPlanetResponse getPlanetsByName(@PathVariable("name") String name);
}