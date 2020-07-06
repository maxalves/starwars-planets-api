package fixtures;

import com.challenge.starwars.models.external.SwapiPlanet;
import com.challenge.starwars.models.external.SwapiPlanetResponse;
import com.github.javafaker.Faker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwapiFixtures {

    public static String SWAPI_INCORRECT_URL = "http://swapi.dev/incorrect";

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

    public static SwapiPlanet getKnownSwapiPlanet() {
        return SwapiPlanet.builder()
                .url("http://swapi.dev/api/planets/8/")
                .name("Naboo")
                .terrain("grassy hills, swamps, forests, mountains")
                .gravity("1 standard")
                .climate("temperate")
                .diameter(12120)
                .surfaceWater(12)
                .edited(LocalDateTime.now())
                .created(LocalDateTime.now())
                .orbitalPeriod(312)
                .rotationPeriod(26)
                .films(Arrays.asList(
                        "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/",
                        "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .residents(Arrays.asList(
                        "http://swapi.dev/api/people/3/",
                        "http://swapi.dev/api/people/21/",
                        "http://swapi.dev/api/people/35/",
                        "http://swapi.dev/api/people/36/",
                        "http://swapi.dev/api/people/37/",
                        "http://swapi.dev/api/people/38/",
                        "http://swapi.dev/api/people/39/",
                        "http://swapi.dev/api/people/42/",
                        "http://swapi.dev/api/people/60/",
                        "http://swapi.dev/api/people/61/",
                        "http://swapi.dev/api/people/66/"))
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

        for(int planetsCount = 0; planetsCount < Faker.instance().number().randomDigitNotZero(); planetsCount++)
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
