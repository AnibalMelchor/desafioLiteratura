package com.melchor.aluracursos.literatura.principal;

import com.melchor.aluracursos.literatura.model.Autor;
import com.melchor.aluracursos.literatura.model.Datos;
import com.melchor.aluracursos.literatura.model.DatosLibros;
import com.melchor.aluracursos.literatura.model.Libros;
import com.melchor.aluracursos.literatura.repository.AutorRepository;
import com.melchor.aluracursos.literatura.repository.LibrosRepository;
import com.melchor.aluracursos.literatura.service.ConsumoAPI;
import com.melchor.aluracursos.literatura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;


public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvierteDatos conversor = new ConvierteDatos();

    private final LibrosRepository libroRepository;
    private final AutorRepository autorRepository;

    public Principal(LibrosRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ----------------
                    Elija la opción a travez de su número
                    1 - Buscar libro por titulo
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    ----------------
                    """;
            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
            }catch (InputMismatchException e) {
                System.out.println("Por favor solo ingresa numeros validos.");
            }
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    guardarDatosLibro();
                    break;
                case 2:
                    ListarLibrosRegistrados();
                    break;
                case 3:
                    ListarAutoresRegistrados();
                    break;
                case 4:
                    ListarAutoresVivos();
                    break;
                case 5:
                    ListarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private Datos obetenerDatosLibros() {
        System.out.println("Ingrese el nombre del libro a buscar: ");
        String nombre = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + nombre.toLowerCase());
        Datos datosLibro = conversor.obtenderDatos(json, Datos.class);
        System.out.println(datosLibro);
        return datosLibro;
    }
    public void guardarDatosLibro() {
        Datos datosLibro = obetenerDatosLibros();
        if (datosLibro.results().size() > 0) {
            DatosLibros datoLibro = datosLibro.results().get(0);
            String titulo = datoLibro.titulo();
            Optional<Libros> libroEncontrado = libroRepository.findByTitulo(titulo);
            if (libroEncontrado.isPresent()) {
                System.out.println("El libro " + titulo + " ya se encuentra registrado.");
            } else {
                Libros libro = new Libros(datoLibro);
                Autor autor = new Autor(datoLibro.autor().get(0));
                Optional<Autor> AutorEncontrado = autorRepository.findByNombre(autor.getNombre());
                if (!AutorEncontrado.isPresent()) {
                    autor.agregarLibro(libro);
                    autorRepository.save(autor);
                    InformacionLibro(libro);
                }else{
                    libro.setAutor(AutorEncontrado.get());
                    libroRepository.save(libro);
                    InformacionLibro(libro);
                }
            }
        } else {
            System.out.println("Ingresa el nombre de un libro valido.");
        }
    }

    private void ListarLibrosRegistrados() {
        List<Libros> listaLibros = libroRepository.findAll();
        if (listaLibros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        }
        InformacionLibros(listaLibros);
    }
    private void ListarAutoresRegistrados() {
        List<Autor> listaAutores = autorRepository.findAll();
        if (listaAutores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        }
        InformacionAutores(listaAutores);
    }

    private void ListarAutoresVivos() {
        System.out.println("Ingresa el año vivo del autor(es) que desea buscar: ");

        try {
            var anio = teclado.nextInt();
            List<Autor> listaAutores = autorRepository.findAllByFechaDeFallecimientoAfter(Integer.parseInt(String.valueOf(anio)));
            if (listaAutores.isEmpty()) {
                System.out.println("No hay autores que estuvieran vivos antes de esa fecha");
            }
            InformacionAutores(listaAutores);
        }catch (InputMismatchException e) {
            System.out.println("Por favor solo ingresa numeros validos.");
        }
    }

    private void ListarLibrosPorIdioma() {
        var menu = """
                    ----------------
                    Ingresa el idioma del libro a buscar:
                    es - español
                    en - Ingles
                    fr - Frances
                    pt - Portuges
                    ----------------
                    """;
        System.out.println(menu);
        var idioma = teclado.nextLine();
        List<Libros> listaLibros = libroRepository.findAllByIdiomas(idioma.toLowerCase());
        if (listaLibros.isEmpty()) {
            System.out.println("Ingresa un idioma valido del menu");
        }
        InformacionLibros(listaLibros);
    }

    public void InformacionLibros(List<Libros> listaLibros) {
        listaLibros.stream()
                .sorted(Comparator.comparing(Libros::getTitulo))
                .forEach(libro -> {
                    System.out.println("------ LIBRO ----");
                    System.out.println(libro.getTitulo());
                    System.out.println("Autor: " + libro.getAutor().getNombre());
                    System.out.println("Idiomas: " + libro.getIdiomas());
                    System.out.println("Numero de descargas: " + libro.getNumeroDescargas());
                    System.out.println("------------------\n");
                });
    }

    public void InformacionLibro(Libros libro) {
        System.out.println("-------- Libro ---------");
        System.out.println("Titulo: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor().getNombre());
        System.out.println("Idiomas: " + libro.getIdiomas());
        System.out.println("Numero de descargas: " + libro.getNumeroDescargas());
        System.out.println("-----------------------\n");
    }

    public void InformacionAutores(List<Autor> listaAutores) {
        listaAutores.stream()
                .sorted(Comparator.comparing(Autor::getNombre))
                .forEach(autor -> {
                    System.out.println("------ Autor ---------");
                    System.out.println("Autor: " + autor.getNombre());
                    System.out.println("Fecha de Nacimiento: " + autor.getFechaDeNacimiento());
                    System.out.println("Fecha de fallecimiento: " + autor.getFechaDeFallecimiento());
                    System.out.println("Libros: " + autor.getLibros());
                    System.out.println("---------------------\n");
                });
    }
}
