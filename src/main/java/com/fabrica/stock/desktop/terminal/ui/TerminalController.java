package com.fabrica.stock.desktop.terminal.ui;

import com.fabrica.stock.desktop.terminal.api.ProductoResponse;
import com.fabrica.stock.desktop.terminal.api.TerminalApi;
import com.fabrica.stock.desktop.terminal.api.TerminalResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TerminalController {

    private final TerminalApi terminalApi;
    private final Long maquinaId;

    @FXML
    private Label nombreMaquina;

    @FXML
    private Label estadoMaquina;

    @FXML
    private Label fechaCambioEstado;

    @FXML
    private VBox contenido;

    @FXML
    private HBox acciones;

    public TerminalController(TerminalApi terminalApi, Long maquinaId) {
        this.terminalApi = terminalApi;
        this.maquinaId = maquinaId;
    }

    @FXML
    private void initialize() throws IOException, InterruptedException {
        cargarTerminal();
    }

    private void cargarTerminal() throws IOException, InterruptedException {
        TerminalResponse response = terminalApi.obtener(maquinaId);
        nombreMaquina.setText(response.nombreMaquina());
        estadoMaquina.setText(response.estadoMaquina());
        fechaCambioEstado.setText(response.fechaCambioEstado().toString());

        contenido.getChildren().clear();
        acciones.getChildren().clear();

        switch(estadoMaquina.getText()) {
            case "APTA_PRODUCCION" -> aptaProduccion(response);
            case "FUERA_SERVICIO", "CAMBIO_MOLDE", "MANTENIMIENTO" -> estadosUsuario();
        }
    }

    private void aptaProduccion(TerminalResponse response) {
        Label titulo = new Label("Producto activo");
        ListView<ProductoResponse> productos = new ListView<>();
        productos.getItems().addAll(response.productos().productos());
        productos.setMaxWidth(Double.MAX_VALUE);

        Button asignarProducto = crearBoton("Probando", "CAMBIO_MOLDE");
        contenido.getChildren().addAll(titulo, productos, asignarProducto);

        productos.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(ProductoResponse producto, boolean empty) {
                super.updateItem(producto, empty);

                if(empty || producto == null) setText(null);
                else setText(producto.codigo() + " - " + producto.descripcion());
            }
        });

        Button cambioDeMolde = crearBoton("Cambio de Molde", "CAMBIO_MOLDE");
        acciones.getChildren().add(cambioDeMolde);

        Button mantenimiento = crearBoton("Mantenimiento", "MANTENIMIENTO");
        acciones.getChildren().add(mantenimiento);

        Button fueraServicio = crearBoton("Fuera de Servicio", "FUERA_SERVICIO");
        acciones.getChildren().add(fueraServicio);
    }

    private void estadosUsuario() {
        String estado = estadoMaquina.getText();

        Button aptaProduccion = crearBoton("Apta producción", "APTA_PRODUCCION");
        acciones.getChildren().add(aptaProduccion);

        switch(estado) {
            case "FUERA_SERVICIO" -> {
                Button cambioDeMolde = crearBoton("Cambio de molde", "CAMBIO_MOLDE");
                acciones.getChildren().add(cambioDeMolde);

                Button mantenimiento = crearBoton("Mantenimiento", "MANTENIMIENTO");
                acciones.getChildren().add(mantenimiento);
            }

            case "MANTENIMIENTO" -> {
                Button cambioDeMolde = crearBoton("Cambio de Molde", "CAMBIO_MOLDE");
                acciones.getChildren().add(cambioDeMolde);

                Button fueraServicio = crearBoton("Fuera de Servicio", "FUERA_SERVICIO");
                acciones.getChildren().add(fueraServicio);
            }

            case "CAMBIO_MOLDE" -> {
                Button mantenimiento = crearBoton("Mantenimiento",  "MANTENIMIENTO");
                acciones.getChildren().add(mantenimiento);

                Button fueraServicio = crearBoton("Fuera de Servicio", "FUERA_SERVICIO");
                acciones.getChildren().add(fueraServicio);
            }
        }
    }

    private Button crearBoton(String nombreBoton, String estado) {
        Button boton = new Button(nombreBoton);
        boton.setOnAction(e -> {
            try {
                terminalApi.cambiarEstado(maquinaId, estado);
                cargarTerminal();
            } catch(IOException | InterruptedException ex) { throw new RuntimeException(ex); }
        });
        return boton;
    }
}