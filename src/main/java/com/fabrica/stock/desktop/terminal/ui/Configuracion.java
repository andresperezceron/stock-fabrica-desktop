package com.fabrica.stock.desktop.terminal.ui;

import com.fabrica.stock.desktop.terminal.api.TerminalResponse;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Configuracion {
    private final TerminalResponse response;
    private final VBox contenido;
    private final HBox acciones;
    private final Runnable estadosUsuario;

    public Configuracion(TerminalResponse response, VBox contenido, HBox acciones, Runnable estadosUsuario) {
        this.response = response;
        this.contenido = contenido;
        this.acciones = acciones;
        this.estadosUsuario = estadosUsuario;
        crear();
    }

    public void crear() {
        Label labProductoActivo = new Label("Producto activo: " + response.config().productoDesc());
        contenido.getChildren().add(labProductoActivo);

        Button cambiarEstado = new Button("Cambiar estado");
        cambiarEstado.setOnAction(e -> estadosUsuario.run());

        acciones.getChildren().add(cambiarEstado);
    }
}
