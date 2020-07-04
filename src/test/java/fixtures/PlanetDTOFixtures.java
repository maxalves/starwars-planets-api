package fixtures;

import com.challenge.starwars.models.dtos.PlanetDTO;
import com.github.javafaker.Faker;

public class PlanetDTOFixtures {
    public static PlanetDTO getRandomPlanet() {
        return PlanetDTO.builder()
                .name(Faker.instance().space().planet())
                .climate(Faker.instance().weather().description())
                .terrain(Faker.instance().funnyName().name())
                .build();
    }

    public static PlanetDTO getRandomStarWarsPlanet() {
        return PlanetDTO.builder()
                .name("Alderaan")
                .climate(Faker.instance().weather().description())
                .terrain(Faker.instance().funnyName().name())
                .build();
    }

    public static PlanetDTO getPlanetMissingRequiredFields() {
        return PlanetDTO.builder()
                .climate(Faker.instance().weather().description())
                .terrain(Faker.instance().funnyName().name())
                .build();
    }
}
