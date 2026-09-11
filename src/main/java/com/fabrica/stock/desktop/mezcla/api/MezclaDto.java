package com.fabrica.stock.desktop.mezcla.api;

import java.time.LocalDateTime;

public record MezclaDto(
        Long id,
        Long productoId,
        String productoCodigo,
        String productoDesc,
        LocalDateTime fechaCreacion
) {}