package com.challenge.starwars.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Relation(collectionRelation = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class PlanetDTO extends RepresentationModel<PlanetDTO> {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String id;

    @JsonProperty(required = true)
    @NotBlank(message = "Name is required")
    @Size(max = 120)
    String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 200)
    String climate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(max = 200)
    String terrain;
}
