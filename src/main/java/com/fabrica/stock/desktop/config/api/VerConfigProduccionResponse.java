package com.fabrica.stock.desktop.config.api;

import java.util.List;

public record VerConfigProduccionResponse(
        String productoDesc,
        Integer cajasPorPalet,
        List<PaletMateriaPrimaDto> paletsMateriaPrima,
        String observaciones
){}