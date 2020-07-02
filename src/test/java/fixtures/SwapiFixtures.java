package fixtures;

import com.challenge.starwars.models.external.SwapiPlanet;
import com.challenge.starwars.models.external.SwapiPlanetResponse;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SwapiFixtures {

    public static SwapiPlanet getRandomSwapiPlanet() {
        return SwapiPlanet.builder()
                .url(Faker.instance().regexify("[a-zA-Z0-9]{10}"))
                .name(Faker.instance().regexify("[a-zA-Z0-9]{10}"))
                .terrain(Faker.instance().regexify("[a-zA-Z]{10}"))
                .gravity(Faker.instance().regexify("[a-zA-Z]{10}"))
                .climate(Faker.instance().regexify("[a-zA-Z]{10}"))
                .diameter(Faker.instance().number().randomDigitNotZero())
                .surfaceWater(Faker.instance().number().randomDigitNotZero())
                .edited(LocalDateTime.now())
                .created(LocalDateTime.now())
                .orbitalPeriod(Faker.instance().number().randomDigitNotZero())
                .rotationPeriod(Faker.instance().number().randomDigitNotZero())
                .films(getRandomResources())
                .residents(getRandomResources())
                .build();
    }

    private static List<String> getRandomResources() {
        var resources = new ArrayList<String>();

        for(int resourcesCount = 0; resourcesCount < Faker.instance().number().randomDigit(); resourcesCount++)
            resources.add(Faker.instance().regexify("[a-zA-Z]{10}"));

        return resources;
    }

    private static List<SwapiPlanet> getRandomSwapiPlanets() {
        var swapiPlanets = new ArrayList<SwapiPlanet>();

        for(int planetsCount = 0; planetsCount < Faker.instance().number().randomDigit(); planetsCount++)
            swapiPlanets.add(getRandomSwapiPlanet());

        return swapiPlanets;
    }

    public static SwapiPlanetResponse getRandomSwapiPlanetResponse() {
        var planets = getRandomSwapiPlanets();
        return SwapiPlanetResponse.builder()
                .nextPage(Faker.instance().regexify("[a-zA-Z0-9]{10}"))
                .previousPage(Faker.instance().regexify("[a-zA-Z0-9]{10}"))
                .results(planets)
                .resultsSize(planets.size())
                .build();
    }

    public static SwapiPlanetResponse getEmptySwapiPlanetResponse() {
        return SwapiPlanetResponse.builder()
                .nextPage(null)
                .previousPage(null)
                .results(new ArrayList<>())
                .resultsSize(0)
                .build();
    }
}
