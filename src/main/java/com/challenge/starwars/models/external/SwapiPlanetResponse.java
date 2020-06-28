package com.challenge.starwars.models.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwapiPlanetResponse {
    @JsonProperty(value="count")
    private Integer resultsSize;

    @JsonProperty(value="next")
    private String nextPage;

    @JsonProperty(value="previous")
    private String previousPage;

    private List<SwapiPlanet> results;
}
