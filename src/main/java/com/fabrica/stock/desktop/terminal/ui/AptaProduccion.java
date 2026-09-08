package com.fabrica.stock.desktop.terminal.ui;

import com.fabrica.stock.desktop.terminal.api.ProductoResponse;
import com.fabrica.stock.desktop.terminal.api.TerminalApi;
import com.fabrica.stock.desktop.terminal.api.TerminalResponse;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AptaProduccion {
    private final TerminalApi terminalApi;
    private final Long maquinaId;
    private final VBox contenidos;
    private final HBox acciones;
    private final TerminalResponse response;
    private final Runnable refrescar;
    private final Runnable estadosUsuario;

    public AptaProduccion(
            TerminalApi terminalApi,
            Long maquinaId,
            VBox contenidos,
            HBox acciones,
            TerminalResponse response,
            Runnable refrescar,
            Runnable estadosUsuario
    )
    {
        this.terminalApi = terminalApi;
        this.maquinaId = maquinaId;
        this.contenidos = contenidos;
        this.acciones = acciones;
        this.response = response;
        this.refrescar = refrescar;
        this.estadosUsuario = estadosUsuario;
        crear();
    }

    private void crear() {
        Label titulo = new Label("Selección de producto a fabricar");

        ListView<ProductoResponse> productos = new ListView<>();
        productos.getItems().addAll(response.productos().productos());
        productos.setMaxWidth(Double.MAX_VALUE);
        productos.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(ProductoResponse producto, boolean empty) {
                super.updateItem(producto, empty);
                if(empty || producto == null) setText(null);
                else setText(producto.codigo() + " - " + producto.descripcion());
            }
        });

        contenidos.getChildren().addAll(titulo, productos);

        Button asignar = new Button("Asignar producto");
        asignar.setOnAction(e -> {
            try {
                Long productoId = productos.getSelectionModel().getSelectedItem().id();
                terminalApi.asignarProducto(maquinaId, productoId);
                refrescar.run();
            }catch(IOException | InterruptedException ex) { throw new RuntimeException(ex); }
        });

        asignar.setDisable(true);

        productos.getSelectionModel().selectedItemProperty().addListener(
                (observable,
                 anterior,
                 seleccionado) -> {
                    asignar.setDisable(seleccionado == null);
                }
        );

        Button cambiarEstado = new Button("Cambiar estado");
        cambiarEstado.setOnAction(e -> estadosUsuario.run());

        acciones.getChildren().addAll(asignar, cambiarEstado);
    }
}