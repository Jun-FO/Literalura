package com.aluracursos.literalura.service;

import com.aluracursos.literalura.model.Authors;
import com.aluracursos.literalura.model.Books;
import com.aluracursos.literalura.repository.AuthorsRepository;
import com.aluracursos.literalura.repository.BooksRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Gutendex {

    private static final String API_BASE_URL = "http://gutendex.com/books/?search=";

    private final AuthorsRepository authorsRepository;

    private final BooksRepository booksRepository;

    @Autowired
    public Gutendex(AuthorsRepository authorsRepository, BooksRepository booksRepository) {
        this.authorsRepository = authorsRepository;
        this.booksRepository = booksRepository;
    }

    public List<Books> searchBooksByTitle(String title) {
        if (title != null && !title.isEmpty()) {
            String url = buildSearchUrl(title);
            String jsonResponse = obtainData(url);
            List<Books> booksList = parseBooksFromJson(jsonResponse).stream()
                    .filter(book -> book.getTitle().equalsIgnoreCase(title))
                    .limit(1)
                    .collect(Collectors.toList());
            booksRepository.saveAll(booksList);
            return booksList;
        } else {
            return Collections.emptyList();
        }
    }

    public List<Books> getRegisteredBooks() {
        return booksRepository.findAll();
    }

    private String buildSearchUrl(String title) {
        return API_BASE_URL + title.replaceAll(" ", "%20");
    }

    private String obtainData(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Books> parseBooksFromJson(String json) {
        List<Books> booksList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode resultNode = rootNode.path("results");
            Iterator<JsonNode> elements = resultNode.elements();

            while (elements.hasNext()) {
                JsonNode bookNode = elements.next();
                Books book = new Books();
                book.setTitle(bookNode.path("title").asText());

                JsonNode authorNode = bookNode.path("authors").elements().next();
                String authorName = authorNode.path("name").asText();
                Integer birthYear = authorNode.path("birth_year").isInt() ? authorNode.path("birth_year").intValue() : null;
                Integer deathYear = authorNode.path("death_year").isInt() ? authorNode.path("death_year").intValue() : null;

                Authors author = authorsRepository.findByName(authorName);
                if (author == null) {
                    author = new Authors();
                    author.setName(authorName);
                    author.setBirth_year(Integer.valueOf(birthYear));
                    author.setDeath_year(Integer.valueOf(deathYear));
                    authorsRepository.save(author);
                }
                book.setAuthor(author);

                String authors = bookNode.path("authors").elements().hasNext() ?
                        bookNode.path("authors").elements().next().path("name").asText() : "Unknown";
                book.setAuthors(authors);

                String language = bookNode.path("languages").elements().hasNext() ?
                        bookNode.path("languages").elements().next().asText() : "Unknown";
                book.setLanguages(language);

                book.setDownloads(bookNode.path("download_count").asInt());

                booksList.add(book);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return booksList;
    }

    public List<Authors> getAuthorsByYearRange(int startYear, int endYear) {
        List<Authors> allAuthors = authorsRepository.findAll();
        return allAuthors.stream()
                .filter(author -> {
                    Integer birthYear = author.getBirth_year();
                    Integer deathYear = author.getDeath_year();
                    return birthYear != null && birthYear <= endYear && (deathYear == null || deathYear > startYear);
                })
                .collect(Collectors.toList());
    }
}
