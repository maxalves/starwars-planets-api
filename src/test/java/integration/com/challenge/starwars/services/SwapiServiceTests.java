package integration.com.challenge.starwars.services;

import com.challenge.starwars.services.external.StarWarsService;
import fixtures.SwapiFixtures;
import integration.com.challenge.starwars.StarWarsApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SwapiServiceTests extends StarWarsApplicationTests {

    @Autowired
    StarWarsService starWarsService;

    @Test
    public void getFilmApparitionsCount_StarWarsPlanetName_Number() {
        var planet = SwapiFixtures.getKnownSwapiPlanet();
        var filmCount = starWarsService.getFilmsCount(planet.getName());

        assertTrue(filmCount.isPresent());
        assertEquals(planet.getFilms().size(), filmCount.get());
    }

    @Test
    public void getFilmApparitionsCount_NotAStarWarsPlanetName_Zero() {
        var filmCount = starWarsService.getFilmsCount("Terra");

        assertTrue(filmCount.isPresent());
        assertEquals(0, filmCount.get());
    }
}
