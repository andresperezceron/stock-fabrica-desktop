package com.fabrica.stock.desktop.config.api;

public record PaletMateriaPrimaDto(
        String matricula,
        String productoDesc,
        String productoCodigo,
        String loteContenido
) {}