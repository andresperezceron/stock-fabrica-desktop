package com.fabrica.stock.desktop.terminal.ui;

import com.fabrica.stock.desktop.terminal.api.TerminalApi;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EstadosUsuario {
    private final TerminalApi terminalApi;
    private final Long maquinaId;
    private final VBox contenidos;
    private final HBox acciones;
    private final Runnable refrescar;

    public EstadosUsuario(
            TerminalApi terminalApi,
            Long maquinaId,
            VBox contenidos,
            HBox acciones,
            Runnable refrescar

    )
    {
        this.terminalApi = terminalApi;
        this.maquinaId = maquinaId;
        this.contenidos = contenidos;
        this.acciones = acciones;
        this.refrescar = refrescar;
        crear();
    }

    private void crear() {
        Button cambioDeMolde = crearBoton("Cambio de Molde", "CAMBIO_MOLDE");
        contenidos.getChildren().add(cambioDeMolde);

        Button mantenimiento = crearBoton("Mantenimiento",  "MANTENIMIENTO");
        contenidos.getChildren().add(mantenimiento);

        Button fueraServicio = crearBoton("Fuera de Servicio", "FUERA_SERVICIO");
        contenidos.getChildren().add(fueraServicio);

        Button aptaProduccion = crearBoton("Apta producción", "APTA_PRODUCCION");
        acciones.getChildren().add(aptaProduccion);
    }

    private Button crearBoton(String nombreBoton, String estado) {
        Button boton = new Button(nombreBoton);
        boton.setOnAction(e -> {
            try {
                terminalApi.cambiarEstado(maquinaId, estado);
                refrescar.run();
            }catch(IOException | InterruptedException ex) { throw new RuntimeException(ex); }
        });
        return boton;
    }
}
