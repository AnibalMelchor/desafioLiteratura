package com.melchor.aluracursos.literatura.repository;

import com.melchor.aluracursos.literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombre(String nombre);

    List<Autor> findAllByFechaDeFallecimientoAfter(Integer anio);
}
