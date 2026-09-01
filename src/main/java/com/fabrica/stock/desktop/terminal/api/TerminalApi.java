package com.fabrica.stock.desktop.terminal.api;

import com.fabrica.stock.desktop.infrastructure.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.Map;

public class TerminalApi {
    private final ApiClient apiClient;
    private final ObjectMapper objectMapper;

    public TerminalApi(ApiClient apiClient, ObjectMapper objectMapper) {
        this.apiClient = apiClient;
        this.objectMapper = objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
    }

    public TerminalResponse obtener(Long maquinaId) throws IOException, InterruptedException {

        String json = apiClient.get(
                "http://localhost:8080/api/maquinas/"
                        + maquinaId
                        + "/terminal"
        );

        return objectMapper.readValue(json, TerminalResponse.class);
    }

    public void cambiarEstado(Long maquinaId, String estado) throws IOException, InterruptedException {
        String json = objectMapper.writeValueAsString(Map.of("estado", estado));
        apiClient.put("http://localhost:8080/api/maquinas/" + maquinaId + "/estado", json);
    }

    public void asignarProducto(Long maquinaId, Long productoId)
            throws IOException, InterruptedException {

        String json = objectMapper.writeValueAsString(
                new IdProductoRequest(productoId)
        );

        apiClient.post(
                "http://localhost:8080/api/maquinas/"
                        + maquinaId
                        + "/config",
                json
        );
    }
}
