package com.challenge.starwars.controllers;

import com.challenge.starwars.models.Planet;
import com.challenge.starwars.models.dtos.PlanetDTO;
import com.challenge.starwars.services.PlanetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
        var modelMapper = new ModelMapper();

        var createdPlanet = planetService.create(modelMapper.map(planet, Planet.class));

        Link selfLink = linkTo(methodOn(this.getClass()).findPlanet(createdPlanet.getId())).withSelfRel();

        return modelMapper.map(createdPlanet, PlanetDTO.class).add(selfLink);
    }

    @GetMapping("/{id}")
    PlanetDTO findPlanet(@PathVariable("id") String planetId) {
        return null;
    }
}
