package com.fabrica.stock.desktop.terminal.api;

public record TerminalResponse(
        String nombreMaquina,
        String estadoMaquina,
        ProductosInyeccionResponse productos
){}