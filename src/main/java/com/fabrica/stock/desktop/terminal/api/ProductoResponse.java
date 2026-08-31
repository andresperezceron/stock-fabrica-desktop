package com.fabrica.stock.desktop.terminal.api;

public record ProductoResponse(
        Long id,
        String codigo,
        String descripcion
){}
