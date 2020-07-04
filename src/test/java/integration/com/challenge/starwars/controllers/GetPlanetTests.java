package integration.com.challenge.starwars.controllers;

import com.challenge.starwars.repositories.PlanetRepository;
import com.github.javafaker.Faker;
import fixtures.PlanetDTOFixtures;
import integration.com.challenge.starwars.StarWarsApplicationTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class GetPlanetTests extends StarWarsApplicationTests {

    private final MockMvc mockMvc;

    private final PlanetRepository planetRepository;

    @AfterEach
    void clear() {
        planetRepository.deleteAll();
    }

    @Autowired
    public GetPlanetTests(MockMvc mockMvc, PlanetRepository planetRepository) {
        this.mockMvc = mockMvc;
        this.planetRepository = planetRepository;
    }

    @Test
    void getPlanet_PlanetExists_FoundPlanet() throws Exception {
        var result = mockMvc.perform(
                post("/planets")
                .contentType("application/json")
                .content(convertToJson(PlanetDTOFixtures.getRandomPlanet())))
                .andExpect(status().isCreated())
                .andReturn();

        var planet = convertToPlanetDTO(result);

        mockMvc.perform(
                get("/planets/{id}", planet.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.planets").exists());

        assertEquals(1, planetRepository.count());
        assertNotNull(planetRepository.findByName(planet.getName()));
    }

    @Test
    void getPlanet_PlanetDoesNotExist_PlanetNotFound() throws Exception {
        var randomId = Faker.instance().random().hex();

        mockMvc.perform(
                get("/planets/{id}", randomId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());
    }

}
