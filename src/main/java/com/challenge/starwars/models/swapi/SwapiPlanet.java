package com.challenge.starwars.models.swapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwapiPlanet {
    private String url;

    private String name;

    private String terrain;

    private String gravity;

    private String climate;

    private Integer diameter;

    private Integer surfaceWater;

    private LocalDateTime edited;

    private LocalDateTime created;

    private Integer orbitalPeriod;

    private Integer rotationPeriod;

    private List<String> films;

    private List<String> residents;
}
