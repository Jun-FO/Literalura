package com.aluracursos.literalura.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String authors;
    private String languages;
    private String copyright;
    private Integer downloads;
    private String authorBirthDate;
    private String authorDeathDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Authors author;

    @Override
    public String toString() {
        return
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", languages='" + languages + '\'' +
                ", copyright='" + copyright + '\'' +
                ", downloads=" + downloads +
                ", authorBirthDate='" + authorBirthDate + '\'' +
                ", authorDeathDate='" + authorDeathDate + "'";
    }

    public LocalDate getParsedAuthorBirthDate() {
        return parseLocalDate(authorBirthDate);
    }

    public LocalDate getParsedAuthorDeathDate() {
        return parseLocalDate(authorDeathDate);
    }

    private LocalDate parseLocalDate(String dateString) {
        try {
            int year = Integer.parseInt(dateString);
            return LocalDate.of(year, 1, 1);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public String getAuthorBirthDate() {
        return authorBirthDate;
    }

    public void setAuthorBirthDate(String authorBirthDate) {
        this.authorBirthDate = authorBirthDate;
    }

    public String getAuthorDeathDate() {
        return authorDeathDate;
    }

    public void setAuthorDeathDate(String authorDeathDate) {
        this.authorDeathDate = authorDeathDate;
    }

    public Authors getAuthor() {
        return author;
    }

    public void setAuthor(Authors author) {
        this.author = author;
    }
}