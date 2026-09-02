package com.fabrica.stock.desktop.terminal.api;

import java.util.List;

public record VerConfigProduccionResponse(
        String productoCodigo,
        String productoDesc,
        Integer cajasPorPalet,
        List<PaletMateriaPrimaDto> paletsMateriaPrima,
        String observaciones
){}