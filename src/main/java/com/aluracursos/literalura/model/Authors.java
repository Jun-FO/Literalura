package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Authors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private Integer birth_year;
    private Integer death_year;

    @Override
    public String toString() {
        return
                "name='" + name + '\'' +
                ", birth_year=" + birth_year + '\'' +
                ", death_year=" + death_year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(Integer birth_year) {
        this.birth_year = birth_year;
    }

    public Integer getDeath_year() {
        return death_year;
    }

    public void setDeath_year(Integer death_year) {
        this.death_year = death_year;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
