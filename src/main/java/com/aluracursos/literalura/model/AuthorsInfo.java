package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorsInfo(
        @JsonAlias("Name") String name,
        @JsonAlias("Birth Year") Integer birth_year,
        @JsonAlias("Death Year") Integer death_year
) {
}
