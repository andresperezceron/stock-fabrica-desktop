package com.fabrica.stock.desktop.terminal.api;

public record PaletConsumoInyeccionDto(
        String matricula,
        String descProducto,
        String loteMateriaPrima,
        String nombreUbicacion
){}
