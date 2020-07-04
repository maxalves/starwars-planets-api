package integration.com.challenge.starwars.services;

import com.challenge.starwars.services.external.StarWarsService;
import fixtures.SwapiFixtures;
import integration.com.challenge.starwars.StarWarsApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;

@TestPropertySource(properties = "swapi.url=http://swapi.dev/incorrect_url")
public class CircuitBreakerTests extends StarWarsApplicationTests {

    @Autowired
    StarWarsService starWarsService;

    @Test
    public void getFilmApparitionsCount_StarWarsPlanetName_Number() {
        var planet = SwapiFixtures.getKnownSwapiPlanet();
        var filmCount = starWarsService.getFilmsCount(planet.getName());

        assertFalse(filmCount.isPresent());
    }
}


