package com.challenge.starwars.models;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Planet {
    String id;

    String name;

    String climate;

    String terrain;
}
