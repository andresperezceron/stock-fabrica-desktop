package com.fabrica.stock.desktop.terminal.api;

public record PaletMateriaPrimaDto(
        String matricula,
        String productoDesc,
        String productoCodigo,
        String loteContenido
) {}