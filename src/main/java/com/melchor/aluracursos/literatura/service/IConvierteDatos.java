package com.melchor.aluracursos.literatura.service;

public interface IConvierteDatos {
    <T> T obtenderDatos(String json, Class<T> clase);
}
