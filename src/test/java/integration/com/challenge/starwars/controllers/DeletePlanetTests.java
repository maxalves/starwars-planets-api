package integration.com.challenge.starwars.controllers;

import com.challenge.starwars.models.dtos.PlanetDTO;
import com.challenge.starwars.repositories.PlanetRepository;
import com.github.javafaker.Faker;
import fixtures.PlanetDTOFixtures;
import integration.com.challenge.starwars.StarWarsApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class DeletePlanetTests extends StarWarsApplicationTests {

    private final MockMvc mockMvc;

    private final PlanetRepository planetRepository;

    @Autowired
    public DeletePlanetTests(MockMvc mockMvc, PlanetRepository planetRepository) {
        this.mockMvc = mockMvc;
        this.planetRepository = planetRepository;
    }

    @Test
    void deletePlanet_PlanetExists_FoundPlanet() throws Exception {
        var result = this.mockMvc.perform(
                post("/planets")
                .contentType("application/json")
                .content(convertToJson(PlanetDTOFixtures.getRandomPlanet())))
                .andExpect(status().isCreated())
                .andReturn();

        PlanetDTO planet = convertToPlanetDTO(result);

        mockMvc.perform(
                delete("/planets/{id}", planet.getId()))
                .andExpect(status().isOk());

        assertEquals(0, planetRepository.count());
    }

    @Test
    void deletePlanet_PlanetDoesNotExist_PlanetNotFound() throws Exception {
        var randomId = Faker.instance().random().hex();

        mockMvc.perform(
                delete("/planets/{id}", randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());
    }
}
