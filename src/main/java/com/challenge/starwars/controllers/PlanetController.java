package com.challenge.starwars.controllers;

import com.challenge.starwars.models.Planet;
import com.challenge.starwars.models.dtos.PlanetDTO;
import com.challenge.starwars.services.PlanetService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@Slf4j
@RestController
@RequestMapping("/planets")
public class PlanetController {

    private final PlanetService planetService;
    private final ModelMapper modelMapper;

    @Autowired
    public PlanetController(ModelMapper mapper, PlanetService planetService) {
        this.planetService = planetService;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PlanetDTO addPlanet(@Valid @RequestBody PlanetDTO planet) {
        log.debug(String.format("Saving planet %s...", planet.getName()));

        var createdPlanet = planetService.create(modelMapper.map(planet, Planet.class));

        return mapToPresentation(createdPlanet);
    }

    @GetMapping("/{id}")
    PlanetDTO findPlanet(@PathVariable("id") String planetId) {
        log.debug(String.format("Searching for planet with id %s...", planetId));

        var planet = planetService.findById(planetId);

        return mapToPresentation(planet);
    }

    @GetMapping
    CollectionModel<PlanetDTO> findAllPlanets(
            @Min(1) @RequestParam(value = "page", defaultValue = "1") int page,
            @Min(1) @Max(100) @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "name", required = false) String name) {
        log.debug(String.format("Searching for planets page=%s size=%s name=%s", page, size, name));

        var pageRequest = PageRequest.of(page - 1, size);

        var planets = Objects.isNull(name) ? planetService.findAll(pageRequest) :
                planetService.findAllByName(pageRequest, name);

        return CollectionModel.of(mapToPresentation(planets))
                .add(getPageLink(page, size, name).withSelfRel())
                .addIf(planets.hasPrevious(),
                        () -> getPageLink(page - 1, size, name).withRel("previous"))
                .addIf(planets.hasNext(),
                        () -> getPageLink(page + 1, size, name).withRel("next"));
    }

    @DeleteMapping("/{id}")
    PlanetDTO deletePlanet(@PathVariable("id") String planetId) {
        log.debug(String.format("Deleting planet with id %s...", planetId));

        var planet = planetService.delete(planetId);

        return modelMapper.map(planet, PlanetDTO.class).add(linkTo(this.getClass()).withRel("planets"));
    }

    private Link getPageLink(int page, int size, String name) {
        var base = linkTo(this.getClass())
                .toUriComponentsBuilder()
                .queryParam("page", page)
                .queryParam("size", size);

        if (Objects.nonNull(name))
            base.queryParam("name", name);

        var linkString = base.build().toString();

        return Link.of(linkString);
    }

    private List<PlanetDTO> mapToPresentation(Page<Planet> planets) {
        return planets.toList()
                .stream()
                .map(this::mapToPresentation)
                .collect(Collectors.toList());
    }

    private PlanetDTO mapToPresentation(Planet planet) {
        var selfLink = linkTo(methodOn(this.getClass()).findPlanet(planet.getId())).withSelfRel();
        var planetsLink = linkTo(this.getClass()).withRel("planets");

        return modelMapper.map(planet, PlanetDTO.class).add(selfLink).add(planetsLink);
    }
}
