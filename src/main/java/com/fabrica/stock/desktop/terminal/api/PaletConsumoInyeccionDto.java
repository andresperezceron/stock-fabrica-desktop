package com.fabrica.stock.desktop.terminal.api;

public record PaletConsumoInyeccionDto(
        Long paletId,
        String matricula,
        String codigoProducto,
        String descProducto,
        String loteMateriaPrima,
        String nombreUbicacion
){}
