package com.fabrica.stock.desktop.terminal.api;

import java.time.LocalDateTime;

public record TerminalResponse(
        String nombreMaquina,
        String estadoMaquina,
        LocalDateTime fechaCambioEstado,
        ProductosInyeccionResponse productos,
        PaletsConsumoInyeccionResponse palets,
        VerConfigProduccionResponse config
){}