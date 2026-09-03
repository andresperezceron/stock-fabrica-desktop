package com.fabrica.stock.desktop.terminal.api;

public record PaletMateriaPrimaDto(
        Long paletId,
        String matricula,
        String productoDesc,
        String productoCodigo,
        String loteContenido
) {}