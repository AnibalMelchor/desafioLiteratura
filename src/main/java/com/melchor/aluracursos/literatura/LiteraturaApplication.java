package com.melchor.aluracursos.literatura;

import com.melchor.aluracursos.literatura.principal.Principal;
import com.melchor.aluracursos.literatura.repository.AutorRepository;
import com.melchor.aluracursos.literatura.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(LiteraturaApplication.class, args);
    }
    @Autowired
    private LibrosRepository repository;

    @Autowired
    private AutorRepository autorRepository;

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repository, autorRepository);
        principal.muestraElMenu();
    }
}
