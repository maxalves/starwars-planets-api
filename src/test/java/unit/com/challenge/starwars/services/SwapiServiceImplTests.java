package unit.com.challenge.starwars.services;

import com.challenge.starwars.services.external.SwapiClient;
import com.challenge.starwars.services.external.SwapiServiceImpl;
import com.github.javafaker.Faker;
import fixtures.SwapiFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SwapiServiceImplTests {

    @Mock
    private SwapiClient swapiClient;

    @InjectMocks
    private SwapiServiceImpl swapiService;

    @Test
    void getFilmsCount_NotAStarWarsPlanet_OptionalNumber() {
        var planets = SwapiFixtures.getRandomSwapiPlanetResponse();

        var planet = planets.getResults().get(Faker.instance().number().numberBetween(0, planets.getResultsSize()-1));

        when(swapiClient.getPlanetsByName(anyString())).thenReturn(planets);

        var filmCount = swapiService.getFilmsCount(planet.getName());

        assertTrue(filmCount.isPresent());
        assertEquals(filmCount.get(), planet.getFilms().size());

        verify(swapiClient, times(1)).getPlanetsByName(anyString());
    }

    @Test
    void getFilmsCount_NotAStarWarsPlanet_OptionalZero() {
        var swapiResponse = SwapiFixtures.getEmptySwapiPlanetResponse();

        when(swapiClient.getPlanetsByName(anyString())).thenReturn(swapiResponse);

        var filmCount = swapiService.getFilmsCount(Faker.instance().regexify("[a-zA-Z0-9]{10}"));

        assertTrue(filmCount.isPresent());
        assertEquals(filmCount.get(), 0);

        verify(swapiClient, times(1)).getPlanetsByName(anyString());
    }

}
