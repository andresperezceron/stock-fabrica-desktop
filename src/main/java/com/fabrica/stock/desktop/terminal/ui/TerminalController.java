package com.fabrica.stock.desktop.terminal.ui;

import com.fabrica.stock.desktop.terminal.api.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

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

    private void mostrarEstadosUsuario() {
        contenido.getChildren().clear();
        acciones.getChildren().clear();

        new EstadosUsuario(
                terminalApi,
                maquinaId,
                contenido,
                acciones,
                this::refrescarTerminal
        );
        estadoMaquina.setText("Cambiando estado");
    }

    private void refrescarTerminal() {
        try { cargarTerminal();
        }catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
    }

    private void cargarTerminal() throws IOException, InterruptedException {
        TerminalResponse response = terminalApi.obtener(maquinaId);
        nombreMaquina.setText(response.nombreMaquina());
        estadoMaquina.setText(response.estadoMaquina());
        fechaCambioEstado.setText(response.fechaCambioEstado()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));

        contenido.getChildren().clear();
        acciones.getChildren().clear();

        switch(estadoMaquina.getText()) {
            case "APTA_PRODUCCION" -> new AptaProduccion(
                            terminalApi,
                            maquinaId,
                            contenido,
                            acciones,
                            response,
                            this::refrescarTerminal,
                            this::mostrarEstadosUsuario);

            case "FUERA_SERVICIO", "CAMBIO_MOLDE", "MANTENIMIENTO" -> new EstadosUsuario(
                    terminalApi,
                    maquinaId,
                    contenido,
                    acciones,
                    this::refrescarTerminal);

            case "EN_CONFIGURACION", "CONFIGURADA" ->
                    new Configuracion(response, contenido, acciones, this::mostrarEstadosUsuario);
        }
    }

    private void enConfiguracion(TerminalResponse response) throws IOException, InterruptedException {
        Label tituloAsignadas = new Label("Asignadas");
        Label tituloDisponibles = new Label("Disponibles");

        ListView<PaletMateriaPrimaDto> paletsAsignados = listViewAsignados(response);

        ListView<PaletConsumoInyeccionDto> palets = new ListView<>();
        palets.getItems().addAll(response.palets().palets());
        palets.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        palets.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(PaletConsumoInyeccionDto palet, boolean empty) {
                super.updateItem(palet, empty);
                if(empty || palet == null) setText(null);
                else setText(palet.matricula() + " - " +
                        palet.codigoProducto() + "  " +
                        palet.descProducto() + " - " +
                        palet.loteMateriaPrima() + " - " + palet.nombreUbicacion());
            }
        });

        Button asignar = new Button("Asignar ←");
        Button quitar = new Button("→ Quitar");

        asignar.setDisable(true);
        quitar.setDisable(true);

        palets.getSelectionModel().selectedItemProperty().addListener(
                (observable,
                 anterior,
                 seleccionado) -> {
                    asignar.setDisable(seleccionado == null);
                }
        );

        paletsAsignados.getSelectionModel().selectedItemProperty().addListener(
                (observable,
                 anterior,
                 seleccionado) -> {
                    quitar.setDisable(seleccionado == null);
                }
        );

        asignar.setOnAction(event -> {
            PaletConsumoInyeccionDto paletSeleccionado = palets.getSelectionModel().getSelectedItem();
            try {
                terminalApi.agregarMateriaPrima(maquinaId, paletSeleccionado.paletId());
                cargarTerminal();
            } catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        quitar.setOnAction(event -> {
            PaletMateriaPrimaDto paletSeleccionado = paletsAsignados.getSelectionModel().getSelectedItem();
            try {
                terminalApi.quitarMateriaPrima(maquinaId, paletSeleccionado.paletId());
                cargarTerminal();
            } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        VBox columnaAsignadas = new VBox(5);
        columnaAsignadas.setMaxWidth(Double.MAX_VALUE);
        columnaAsignadas.getChildren().addAll(tituloAsignadas, paletsAsignados);

        VBox columnaBotones = new VBox(10);
        columnaBotones.setAlignment(Pos.CENTER);
        columnaBotones.getChildren().addAll(asignar, quitar);

        VBox columnaDisponibles = new VBox(5);
        columnaDisponibles.setMaxWidth(Double.MAX_VALUE);
        columnaDisponibles.getChildren().addAll(tituloDisponibles, palets);

        HBox materiasPrimas = new HBox(20);
        HBox.setHgrow(columnaAsignadas, Priority.ALWAYS);
        HBox.setHgrow(columnaDisponibles, Priority.ALWAYS);

        materiasPrimas.getChildren().addAll(columnaAsignadas, columnaBotones, columnaDisponibles);

        contenido.getChildren().add(materiasPrimas);

        if(estadoMaquina.getText().equals("CONFIGURADA")) {
            Button iniciarProduccion = new Button("Iniciar Producción");
            acciones.getChildren().add(iniciarProduccion);
        }

        Button aptaProduccion = crearBoton("Apta producción", "APTA_PRODUCCION");
        acciones.getChildren().add(aptaProduccion);

        Button cambioDeMolde = crearBoton("Cambio de Molde", "CAMBIO_MOLDE");
        acciones.getChildren().add(cambioDeMolde);

        Button mantenimiento = crearBoton("Mantenimiento", "MANTENIMIENTO");
        acciones.getChildren().add(mantenimiento);

        Button fueraServicio = crearBoton("Fuera de Servicio", "FUERA_SERVICIO");
        acciones.getChildren().add(fueraServicio);
    }

    private Button crearBoton(String nombreBoton, String estado) {
        Button boton = new Button(nombreBoton);
        boton.setOnAction(e -> {
            try {
                terminalApi.cambiarEstado(maquinaId, estado);
                cargarTerminal();
            }catch(IOException | InterruptedException ex) { throw new RuntimeException(ex); }
        });
        return boton;
    }

    private ListView<PaletMateriaPrimaDto> listViewAsignados(TerminalResponse response) {
        ListView<PaletMateriaPrimaDto> paletsAsignados = new ListView<>();
        paletsAsignados.getItems().addAll(response.config().paletsMateriaPrima());
        paletsAsignados.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        paletsAsignados.setFocusTraversable(false);
        paletsAsignados.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(PaletMateriaPrimaDto item, boolean empty) {
                super.updateItem(item, empty);
                if(empty ||  item == null) setText(null);
                else setText(item.matricula() + " - " +
                        item.productoCodigo() + "  " +
                        item.productoDesc() + " - " +
                        item.loteContenido());
            }
        });

        return paletsAsignados;
    }
}