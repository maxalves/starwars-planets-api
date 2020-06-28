package com.challenge.starwars.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Planet implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    String id;

    String name;

    String climate;

    String terrain;
}
