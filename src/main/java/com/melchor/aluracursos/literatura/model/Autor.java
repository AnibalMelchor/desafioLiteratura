package com.melchor.aluracursos.literatura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Libros> libros;

    public Autor() {
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
        this.libros = new ArrayList<>();
    }
    public void agregarLibro(Libros libro) {
        libros.add(libro);
        libro.setAutor(this);
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }


    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }


    public List<Libros> getLibros() {
        return libros;
    }

}
