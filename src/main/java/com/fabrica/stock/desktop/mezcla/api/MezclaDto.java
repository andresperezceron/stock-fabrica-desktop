package com.fabrica.stock.desktop.mezcla.api;

public record MezclaDto(
        Long id,
        Long productoId,
        String productoCodigo,
        String productoDesc
) {}
