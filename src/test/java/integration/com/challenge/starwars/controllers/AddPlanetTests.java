package integration.com.challenge.starwars.controllers;

import com.challenge.starwars.repositories.PlanetRepository;
import fixtures.PlanetDTOFixtures;
import integration.com.challenge.starwars.StarWarsApplicationTests;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class AddPlanetTests extends StarWarsApplicationTests {

    private final MockMvc mockMvc;

    private final PlanetRepository planetRepository;

    @Autowired
    public AddPlanetTests(MockMvc mockMvc, PlanetRepository planetRepository) {
        this.mockMvc = mockMvc;
        this.planetRepository = planetRepository;
    }

    @AfterEach
    void clear() {
        planetRepository.deleteAll();
    }

    @Test
    void addPlanet_NewPlanet_Created() throws Exception {
        var planet = PlanetDTOFixtures.getRandomPlanet();

        mockMvc.perform(
                post("/planets")
                .contentType("application/json")
                .content(convertToJson(planet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(planet.getName()))
                .andExpect(jsonPath("$.terrain").value(planet.getTerrain()))
                .andExpect(jsonPath("$.climate").value(planet.getClimate()))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.planets").exists());

        assertEquals(1, planetRepository.count());
        assertNotNull(planetRepository.findByName(planet.getName()));
    }

    @Test
    void addPlanet_RegisteredPlanet_Conflict() throws Exception {
        var planet = PlanetDTOFixtures.getRandomPlanet();

        mockMvc.perform(
                post("/planets")
                .contentType("application/json")
                .content(convertToJson(planet)))
                .andExpect(status().isCreated());

        mockMvc.perform(
                post("/planets")
                .contentType("application/json")
                .content(convertToJson(planet)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());

        assertEquals(1, planetRepository.count());
        assertNotNull(planetRepository.findByName(planet.getName()));
    }

    @Test
    void addPlanet_PlanetMissingRequiredFields_BadRequest() throws Exception {
        var planet = PlanetDTOFixtures.getPlanetMissingRequiredFields();

        mockMvc.perform(
                post("/planets")
                .contentType("application/json")
                .content(convertToJson(planet)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());

        assertEquals(0, planetRepository.count());
    }
}
