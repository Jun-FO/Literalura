package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BooksInfo(
        @JsonAlias("ID") Long id,
        @JsonAlias("Title") String title,
        @JsonAlias("Author") String authors,
        @JsonAlias("Language") String languages,
        @JsonAlias("Copyright") String copyright,
        @JsonAlias("Downloads") Integer download_count
) {
}
