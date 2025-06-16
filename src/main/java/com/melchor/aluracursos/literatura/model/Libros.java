package com.melchor.aluracursos.literatura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idiomas;
    private Double numeroDescargas;

    @ManyToOne
    private Autor autor;

    public Libros() {
    }

    public Libros(DatosLibros datosLibros) {
        this.titulo = datosLibros.titulo();
        this.idiomas = String.join(", ", datosLibros.idiomas());
        this.numeroDescargas = datosLibros.numeroDescargas() != null ? datosLibros.numeroDescargas() : 0.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(String idiomas) {
        this.idiomas = idiomas;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
