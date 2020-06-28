package com.challenge.starwars.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomError {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> fields;

    private LocalDateTime timestamp;
}
