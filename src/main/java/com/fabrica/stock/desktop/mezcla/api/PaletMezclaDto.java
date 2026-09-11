package com.fabrica.stock.desktop.mezcla.api;

public record PaletMezclaDto(
        Long paletId,
        String matricula,
        String productoCodigo,
        String productoDesc,
        Integer cantidad,
        String unidadMedida,
        String loteContenido
) {}