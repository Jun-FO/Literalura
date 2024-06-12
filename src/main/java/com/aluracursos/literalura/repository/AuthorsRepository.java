package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorsRepository  extends JpaRepository<Authors, Long> {
    Authors findByName(String name);
}
