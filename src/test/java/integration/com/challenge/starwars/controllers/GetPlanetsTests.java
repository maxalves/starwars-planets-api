package integration.com.challenge.starwars.controllers;

import com.challenge.starwars.repositories.PlanetRepository;
import com.github.javafaker.Faker;
import fixtures.PlanetFixtures;
import integration.com.challenge.starwars.StarWarsApplicationTests;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class GetPlanetsTests extends StarWarsApplicationTests {

    private final MockMvc mockMvc;

    private final PlanetRepository planetRepository;

    private final int PLANETS_REGISTERED_COUNT = 6;

    private final int PAGE_SIZE = PLANETS_REGISTERED_COUNT/3;

    private final String FIRST_PAGE = "1";

    private final String MIDDLE_PAGE = "2";

    private final String LAST_PAGE = "3";

    @Autowired
    public GetPlanetsTests(MockMvc mockMvc, PlanetRepository planetRepository) {
        this.mockMvc = mockMvc;
        this.planetRepository = planetRepository;
    }

    @BeforeAll
    void insertDummyData() {
        var planets = PlanetFixtures.getPlanets(PLANETS_REGISTERED_COUNT);

        planetRepository.deleteAll();
        planetRepository.insert(planets);
    }

    @AfterAll
    void clear() {
        planetRepository.deleteAll();
    }

    @Test
    void getAllPlanets_ExistsPlanetsInPage_FoundPlanets() throws Exception {
        var planetsResult = mockMvc.perform(
                get("/planets")
                        .queryParam("page", FIRST_PAGE)
                        .queryParam("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andReturn();

        var planets = convertToCollection(planetsResult);

        assertEquals(PAGE_SIZE, planets.getContent().size());
    }

    @Test
    void getAllPlanets_NoPlanetInPage_NoPlanets() throws Exception {
        var overPage = Integer.parseInt(LAST_PAGE)+1;

        var planetsResult = mockMvc.perform(
                get("/planets")
                        .queryParam("page", String.valueOf(overPage))
                        .queryParam("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andReturn();

        var planets = convertToCollection(planetsResult);

        assertEquals(0, planets.getContent().size());
    }

    @Test
    void getAllPlanets_Pagination_NavigationLinks() throws Exception {
        var firstPageResult = mockMvc.perform(
                get("/planets")
                        .queryParam("page", FIRST_PAGE)
                        .queryParam("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andReturn();

        var middlePageResult = mockMvc.perform(
                get("/planets")
                        .queryParam("page", MIDDLE_PAGE)
                        .queryParam("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andReturn();

        var lastPageResult = mockMvc.perform(
                get("/planets")
                        .queryParam("page", LAST_PAGE)
                        .queryParam("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isOk())
                .andReturn();

        var firstPage = convertToCollection(firstPageResult);
        var middlePage = convertToCollection(middlePageResult);
        var lastPage = convertToCollection(lastPageResult);

        assertTrue(firstPage.hasLink("next"));
        assertFalse(firstPage.hasLink("previous"));

        assertTrue(middlePage.hasLink("next"));
        assertTrue(middlePage.hasLink("previous"));

        assertFalse(lastPage.hasLink("next"));
        assertTrue(lastPage.hasLink("previous"));
    }

    @Test
    void getAllPlanets_InvalidQueryParameters_RequestError() throws Exception {
        mockMvc.perform(
                get("/planets")
                        .queryParam("page", "-1")
                        .queryParam("size", String.valueOf(PAGE_SIZE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getAllPlanets_ExistsPlanetsContainingName_Planets() throws Exception {
        var planet = planetRepository.findAll(PageRequest.of(0, 1))
                .getContent().get(0).getName();

        var pageResult = mockMvc.perform(
                get("/planets")
                        .queryParam("name", planet))
                .andExpect(status().isOk())
                .andReturn();

        var planetsContainingName = convertToCollection(pageResult);

        assertFalse(planetsContainingName.getContent().isEmpty());
    }

    @Test
    void getAllPlanets_ExistsPlanetsContainingName_NoPlanets() throws Exception {
        var planet = Faker.instance().hitchhikersGuideToTheGalaxy().planet();

        var pageResult = mockMvc.perform(
                get("/planets")
                        .queryParam("name", planet))
                .andExpect(status().isOk())
                .andReturn();

        var planetsContainingName = convertToCollection(pageResult);

        assertTrue(planetsContainingName.getContent().isEmpty());
    }
}
