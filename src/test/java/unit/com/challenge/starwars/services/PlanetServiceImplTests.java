package unit.com.challenge.starwars.services;

import com.challenge.starwars.exceptions.PlanetAlreadyExistsException;
import com.challenge.starwars.exceptions.PlanetNotFoundException;
import com.challenge.starwars.models.Planet;
import com.challenge.starwars.repositories.PlanetRepository;
import com.challenge.starwars.services.PlanetServiceImpl;
import com.challenge.starwars.services.external.StarWarsService;
import com.github.javafaker.Faker;
import fixtures.PlanetFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanetServiceImplTests {

    @Mock
    private PlanetRepository planetRepository;

    @Mock
    private StarWarsService starWarsService;

    @InjectMocks
    private PlanetServiceImpl planetService;

    @Test
    void create_CorrectPlanet_PlanetCreated() {
        var planet = PlanetFixtures.getRandomPlanetWithIdAndFilmApparitionsCount();

        when(planetRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(planetRepository.save(any(Planet.class))).thenReturn(planet);
        when(starWarsService.getFilmsCount(anyString()))
                .thenReturn(Optional.of(planet.getFilmApparitionsCount()));

        var createdPlanet = planetService.create(planet);

        assertEquals(createdPlanet, planet);

        verify(planetRepository, times(1)).findByName(anyString());
        verify(planetRepository, times(1)).save(any(Planet.class));
        verify(starWarsService, times(1)).getFilmsCount(anyString());
    }

    @Test
    void create_ExistingPlanet_PlanetAlreadyExistsException() {
        var planet = PlanetFixtures.getRandomPlanetWithIdAndFilmApparitionsCount();

        when(planetRepository.findByName(anyString())).thenReturn(Optional.of(planet));

        assertThrows(PlanetAlreadyExistsException.class, () -> planetService.create(planet));

        verify(planetRepository, times(1)).findByName(anyString());
        verify(planetRepository, times(0)).save(any(Planet.class));
        verify(starWarsService, times(0)).getFilmsCount(anyString());
    }

    @Test
    void findById_ExistingPlanetId_FoundPlanet() {
        var expectedPlanet = PlanetFixtures.getRandomPlanetWithIdAndFilmApparitionsCount();

        when(planetRepository.findById(anyString())).thenReturn(Optional.of(expectedPlanet));

        var foundPlanet = planetService.findById(expectedPlanet.getId());

        assertEquals(foundPlanet, expectedPlanet);
        verify(planetRepository, times(1)).findById(anyString());
    }

    @Test
    void findById_NonExistentPlanetId_PlanetNotFoundException() {
        when(planetRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(PlanetNotFoundException.class, () -> planetService.findById(anyString()));
        verify(planetRepository, times(1)).findById(anyString());
    }

    @Test
    void findAll_ValidPage_PlanetPage() {
        var page = PlanetFixtures.getRandomPage();
        var planetsPage = PlanetFixtures.getPlanetsPage(page);

        when(planetRepository.findAll(any(Pageable.class))).thenReturn(planetsPage);
        when(starWarsService.getFilmsCount(anyString()))
                .thenReturn(Optional.of(Faker.instance().number().randomDigit()));

        var planets = planetService.findAll(page);

        assertEquals(planets.getNumberOfElements(), page.getPageSize());

        verify(planetRepository, times(1)).findAll(any(Pageable.class));
        verify(starWarsService, times(page.getPageSize())).getFilmsCount(anyString());
    }

    @Test
    void findAll_OverBoundariesPage_EmptyPlanetPage() {
        var page = PlanetFixtures.getRandomPage();
        var expectedPlanetPage = PlanetFixtures.getPlanetsPage(page);

        when(planetRepository.findAll(any(Pageable.class))).thenReturn(expectedPlanetPage);
        when(starWarsService.getFilmsCount(anyString()))
                .thenReturn(Optional.of(Faker.instance().number().randomDigit()));

        var planets = planetService.findAll(page);

        assertEquals(planets.getNumberOfElements(), page.getPageSize());

        verify(planetRepository, times(1)).findAll(any(Pageable.class));
        verify(starWarsService, times(page.getPageSize())).getFilmsCount(anyString());
    }

    @Test
    void findAllByName_ValidPage_PlanetPage() {
        var page = PlanetFixtures.getRandomPage();
        var planetsPage = PlanetFixtures.getPlanetsPage(page);
        when(planetRepository.findAllByNameContainingIgnoreCase(any(Pageable.class), anyString()))
                .thenReturn(planetsPage);

        var planets = planetService.findAllByName(page, "Id");

        assertEquals(planets.getNumberOfElements(), page.getPageSize());

        verify(planetRepository, times(1))
                .findAllByNameContainingIgnoreCase(any(Pageable.class), anyString());
        verify(starWarsService, times(page.getPageSize())).getFilmsCount(anyString());
    }

    @Test
    void deleteById_ExistingPlanetId_FoundPlanet() {
        var expectedPlanet = PlanetFixtures.getRandomPlanetWithIdAndFilmApparitionsCount();

        when(planetRepository.findById(anyString())).thenReturn(Optional.of(expectedPlanet));

        var foundPlanet = planetService.delete(expectedPlanet.getId());

        assertEquals(foundPlanet, expectedPlanet);

        verify(planetRepository, times(1)).findById(anyString());
        verify(planetRepository, times(1)).deleteById(anyString());
    }

    @Test
    void deleteById_NonExistentPlanetId_PlanetNotFoundException() {
        when(planetRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(PlanetNotFoundException.class, () -> planetService.delete(anyString()));

        verify(planetRepository, times(1)).findById(anyString());
        verify(planetRepository, times(0)).deleteById(anyString());
    }
}
