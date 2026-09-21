package com.fabrica.stock.desktop.mezcla.api;

import java.util.List;

public record NuevaMezclaRequest(
        Long productoId,
        List<Long> paletsId
){}