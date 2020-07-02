package fixtures;

import com.challenge.starwars.models.Planet;
import com.github.javafaker.Faker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;


public class PlanetFixtures {

    public static int PAGE_SIZE = Faker.instance().number().randomDigitNotZero();

    public static Planet getRandomPlanet() {
        return Planet.builder()
                .id(Faker.instance().regexify("[a-zA-Z0-9]{10}"))
                .name(Faker.instance().regexify("[a-zA-Z0-9]{10}"))
                .filmApparitionsCount(Faker.instance().number().randomDigit())
                .climate(Faker.instance().regexify("[a-zA-Z]{20}"))
                .terrain(Faker.instance().regexify("[a-zA-Z]{20}"))
                .build();
    }

    public static List<Planet> getPlanets(int quantity) {
        var planets = new ArrayList<Planet>();

        for (int count = 0; count < quantity; count++)
            planets.add(getRandomPlanet());

        return planets;
    }

    public static Pageable getRandomPage() {
        return PageRequest.of(0, PAGE_SIZE);
    }

    public static Page<Planet> getPlanetsPage(Pageable page) {
        var planets = getPlanets(page.getPageSize());
        return new PageImpl<>(planets);
    }
}