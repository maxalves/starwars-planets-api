package com.challenge.starwars.services.external;

import java.util.Optional;

public interface StarWarsService {
    Optional<Integer> getFilmsCount(String planetName);
}
