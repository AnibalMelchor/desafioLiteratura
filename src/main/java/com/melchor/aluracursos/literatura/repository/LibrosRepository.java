package com.melchor.aluracursos.literatura.repository;

import com.melchor.aluracursos.literatura.model.Autor;
import com.melchor.aluracursos.literatura.model.Libros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibrosRepository extends JpaRepository<Libros, Long> {

    Optional<Libros> findByTitulo(String titulo);

    List<Libros> findAllByIdiomas(String idiomas);
}
