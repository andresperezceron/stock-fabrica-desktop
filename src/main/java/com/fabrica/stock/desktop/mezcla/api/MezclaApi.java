package com.fabrica.stock.desktop.mezcla.api;

import com.fabrica.stock.desktop.infrastructure.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class MezclaApi {
    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;

    public MezclaApi(ApiClient apiClient, ObjectMapper objectMapper) {
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
    }

    public MezclasResponse listar() throws IOException, InterruptedException {
        String json = apiClient.get("http://localhost:8080/api/mezclas");
        return objectMapper.readValue(json, MezclasResponse.class);
    }

    public ProductosInyeccionResponse productosInyeccion() throws IOException, InterruptedException {
        String json = apiClient.get("http://localhost:8080/api/productos/inyeccion");
        return objectMapper.readValue(json, ProductosInyeccionResponse.class);
    }
}
