package com.challenge.starwars.controllers;

import com.challenge.starwars.models.Planet;
import com.challenge.starwars.models.dtos.PlanetDTO;
import com.challenge.starwars.services.PlanetService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/planets")
public class PlanetController {

    private final PlanetService planetService;

    @Autowired
    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PlanetDTO addPlanet(@Valid @RequestBody PlanetDTO planet) {
        log.debug(String.format("Saving planet %s...", planet.getName()));
        var modelMapper = new ModelMapper();

        var createdPlanet = planetService.create(modelMapper.map(planet, Planet.class));

        Link selfLink = linkTo(methodOn(this.getClass()).findPlanet(createdPlanet.getId())).withSelfRel();

        return modelMapper.map(createdPlanet, PlanetDTO.class).add(selfLink);
    }

    @GetMapping("/{id}")
    PlanetDTO findPlanet(@PathVariable("id") String planetId) {
        log.debug(String.format("Searching for planet with id %s...", planetId));
        var planet = planetService.findById(planetId);

        Link selfLink = linkTo(methodOn(this.getClass()).findPlanet(planet.getId())).withSelfRel();
        Link planetsLink = linkTo(methodOn(this.getClass()).findAllPlanets()).withRel("planets");

        return new ModelMapper().map(planet, PlanetDTO.class).add(selfLink).add(planetsLink);
    }

    @GetMapping
    List<PlanetDTO> findAllPlanets() {
        return null;
    }
}
