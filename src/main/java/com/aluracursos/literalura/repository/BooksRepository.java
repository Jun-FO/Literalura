package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {
    List<Books> findByTitleContainingIgnoreCase(String title);
}
