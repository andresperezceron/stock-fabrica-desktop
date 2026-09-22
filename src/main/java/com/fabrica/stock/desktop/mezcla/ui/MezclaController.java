package com.fabrica.stock.desktop.mezcla.ui;

import com.fabrica.stock.desktop.mezcla.api.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MezclaController {
    private final MezclaApi mezclaApi;

    @FXML
    private BorderPane root;

    @FXML
    private Label titulo;

    @FXML
    private HBox zonaBotones;

    private final List<String> lMpAsignadas = new ArrayList<>();
    private final List<Long> paletIds = new ArrayList<>();

    public MezclaController(MezclaApi mezclaApi) {
        this.mezclaApi = mezclaApi;
    }

    @FXML
    private void initialize() throws IOException, InterruptedException {
        iniciar();
    }

    private void iniciar() throws IOException, InterruptedException {
        //Label titulo = new Label("MEZCLAS");
        //titulo.setFont(Font.font(24));
        //root.setTop(new StackPane(titulo));
        titulo.setText("MEZCLAS");

        MezclasResponse response = mezclaApi.listar();

        ListView<MezclaDto> mezclas =  new ListView<>();
        mezclas.getItems().addAll(response.mezclas());
        mezclas.setMaxWidth(500);
        mezclas.setMaxHeight(300);
        mezclas.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(MezclaDto mezcla, boolean empty) {
                super.updateItem(mezcla, empty);
                if(empty || mezcla == null) setText(null);
                else setText(mezcla.productoCodigo() + "  -  " + mezcla.productoDesc() + "  -  " +
                        mezcla.fechaCreacion().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
            }
        });

        root.setCenter(mezclas);

        Button bCrear = new Button("Crear Mezcla");
        bCrear.setOnAction(event -> {
            try { creandoMezcla();
            } catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        Button bVer = new Button("Ver");
        bVer.setOnAction(event -> {
            try {
                verMezcla(mezclas.getSelectionModel().getSelectedItem());
            } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        bVer.setDisable(true);
        mezclas.getSelectionModel().selectedItemProperty().addListener((
                        observable,
                        anterior,
                        seleccionado
                ) -> bVer.setDisable(seleccionado == null));

        zonaBotones.getChildren().clear();
        zonaBotones.getChildren().addAll(bCrear, bVer);
    }

    private void verMezcla(MezclaDto item) throws IOException, InterruptedException {
        root.setCenter(null);
        titulo.setText("VISUALIZANDO MEZCLAS");

        Label lbFechaCreacion = MezclaComp.labelFecha(item.fechaCreacion());
        Label lbProducto = MezclaComp.labelProducto(item);
        Label lbComposicion = MezclaComp.labelComposicion();

        VBox vBox = MezclaComp.vBoxBotton();
        vBox.getChildren().addAll(lbFechaCreacion,  lbProducto, lbComposicion);

        root.setCenter(vBox);
        bottonVer();
    }

    private void bottonVer() {
        Button irInicio = new Button("Atrás");
        irInicio.setOnAction(event -> {
            try {
                iniciar();
            } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        zonaBotones.getChildren().clear();
        zonaBotones.getChildren().addAll(irInicio);
    }

    private void creandoMezcla() throws IOException, InterruptedException {
        Label titulo = new Label("CREANDO NUEVA MEZCLA");
        titulo.setFont(Font.font(24));
        root.setTop(new StackPane(titulo));

        ProductosInyeccionResponse response = mezclaApi.productosInyeccion();

        ListView<ProductosInyeccionDto> productos = new ListView<>();
        productos.getItems().addAll(response.productosInyeccion());
        productos.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(ProductosInyeccionDto producto, boolean empty) {
                super.updateItem(producto, empty);
                if(empty || producto == null) setText(null);
                else setText(producto.codigo() + " - " + producto.descripcion());
            }
        });

        productos.setMaxWidth(500);
        productos.setMaxHeight(300);
        root.setCenter(productos);

        Button bAtras = new Button("Atrás");
        bAtras.setOnAction(event -> {
            try { iniciar();
            }catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        Button bSeleccionProducto = new Button("Seleccionar Producto");
        bSeleccionProducto.setOnAction(event -> {
            try {
                seleccionarProducto(
                        productos.getSelectionModel().getSelectedItem().id(),
                        productos.getSelectionModel().getSelectedItem().codigo(),
                        productos.getSelectionModel().getSelectedItem().descripcion());
            }catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });
        bSeleccionProducto.setDisable(true);

        productos.getSelectionModel().selectedItemProperty().addListener((
                        observable,
                        anterior,
                        seleccionado
                ) -> bSeleccionProducto.setDisable(seleccionado == null));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(bAtras,  bSeleccionProducto);
        root.setBottom(hBox);
    }

    private void seleccionarProducto(Long productoId, String productoCodigo, String productoDesc)
            throws IOException, InterruptedException {

        Label idProductoSeleccionado = new Label("Para: " + productoCodigo + " - " + productoDesc);
        Label tituloAsignadas = new Label("Materias primas agregadas");
        Label tituloDisponibles = new Label("Materias primas disponibles");

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.getChildren().addAll(idProductoSeleccionado, tituloAsignadas);
        mostrasAgregadas(vBox, lMpAsignadas);
        vBox.getChildren().addAll(tituloDisponibles);

        PaletsMezclaResponse response = mezclaApi.paletsMezcla();

        ListView<PaletMezclaDto> palets = new ListView<>();
        palets.getItems().addAll(response.paletsMezcla());
        palets.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(PaletMezclaDto palet, boolean empty) {
                super.updateItem(palet, empty);
                if(empty || palet == null) setText(null);
                else setText(palet.matricula() + " - " +
                        palet.productoCodigo() + " - " +
                        palet.productoDesc() + " - " +
                        palet.loteContenido() + " - " +
                        palet.cantidad() + " " +
                        palet.unidadMedida());
            }
        });

        palets.setMaxWidth(500);
        palets.setMaxHeight(300);

        vBox.getChildren().add(palets);
        root.setCenter(vBox);

        Button bAtras = new Button("Atrás");
        bAtras.setOnAction(event -> {
            try {
                creandoMezcla();
                lMpAsignadas.clear();
                paletIds.clear();
            }catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        Button bAgregar = new Button("Agregar");
        bAgregar.setOnAction(event -> {
            try {
                agregarMateriaPrima(
                        palets.getSelectionModel().getSelectedItem(),
                        productoId,
                        productoCodigo,
                        productoDesc);
            }catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        bAgregar.setDisable(true);
        palets.getSelectionModel().selectedItemProperty().addListener((
                observable,
                anterior,
                seleccionado
        ) -> bAgregar.setDisable(seleccionado == null));

        Button bCrear = new Button("Crear");
        bCrear.setOnAction(event -> {
            try { crearMezcla(productoId);
            }catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(bAtras, bAgregar, bCrear);
        root.setBottom(hBox);
    }

    private void crearMezcla(Long productoId) throws IOException, InterruptedException {
        if(paletIds.isEmpty()) return;
        mezclaApi.crearMezcla(productoId, paletIds);
        lMpAsignadas.clear();
        paletIds.clear();
        iniciar();
    }

    private void mostrasAgregadas(VBox vBox, List<String> lMpAsignadas) {
        for(String mpAsignada : lMpAsignadas) {
            vBox.getChildren().add(new Label(mpAsignada));
        }
    }

    private void agregarMateriaPrima(
            PaletMezclaDto item,
            Long id,
            String codigo,
            String desc) throws IOException, InterruptedException {
        lMpAsignadas.add(item.matricula()
                + "  -  " + item.productoCodigo()
                + "  -  " + item.productoDesc()
                + "  -  " + item.loteContenido());
        paletIds.add(item.paletId());
        seleccionarProducto(id, codigo, desc);
    }
}