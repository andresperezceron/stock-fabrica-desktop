package com.fabrica.stock.desktop.mezcla.api;

import com.fabrica.stock.desktop.infrastructure.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class MezclaApi {
    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;

    public MezclaApi(ApiClient apiClient, ObjectMapper objectMapper) {
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
    }

    public MezclasResponse listar() throws IOException, InterruptedException {
        String json = apiClient.get("http://localhost:8080/api/mezclas");
        return objectMapper.readValue(json, MezclasResponse.class);
    }

    public ProductosInyeccionResponse productosInyeccion() throws IOException, InterruptedException {
        String json = apiClient.get("http://localhost:8080/api/productos/inyeccion");
        return objectMapper.readValue(json, ProductosInyeccionResponse.class);
    }

    public PaletsMezclaResponse paletsMezcla() throws IOException, InterruptedException {
        String json = apiClient.get("http://localhost:8080/api/palets/mezcla-inyeccion");
        return objectMapper.readValue(json, PaletsMezclaResponse.class);
    }
}
