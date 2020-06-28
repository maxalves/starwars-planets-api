package com.challenge.starwars.controllers;

import com.challenge.starwars.services.PlanetService;
import com.challenge.starwars.models.Planet;
import com.challenge.starwars.models.dtos.PlanetDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/planets")
public class PlanetController {

    private PlanetService planetService;

    @Autowired
    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PlanetDTO addPlanet(@Valid @RequestBody PlanetDTO planet) {
        var modelMapper = new ModelMapper();

        var createdPlanet = planetService.create(modelMapper.map(planet, Planet.class));

        return modelMapper.map(createdPlanet, PlanetDTO.class);
    }
}
