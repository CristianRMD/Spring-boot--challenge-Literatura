package com.example.literatura.Service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);

}