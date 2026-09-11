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

    public MezclaController(MezclaApi mezclaApi) {
        this.mezclaApi = mezclaApi;
    }

    @FXML
    private void initialize() throws IOException, InterruptedException {
        iniciar();
    }

    private void iniciar() throws IOException, InterruptedException {
        Label titulo = new Label("MEZCLAS");
        titulo.setFont(Font.font(24));
        root.setTop(new StackPane(titulo));

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
            try { crearMezcla();
            } catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        Button bVer = new Button("Ver");
        bVer.setDisable(true);
        mezclas.getSelectionModel().selectedItemProperty().addListener((
                        observable,
                        anterior,
                        seleccionado
                ) -> bVer.setDisable(seleccionado == null));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(bCrear, bVer);
        root.setBottom(hBox);
    }

    private void crearMezcla() throws IOException, InterruptedException {
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
            } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }
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

        List<String> lMpAsignadas = new ArrayList<>();
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
            try { crearMezcla();
            }catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        Button bAgregar = new Button("Agregar");
        bAgregar.setOnAction(event -> {
            try {
                agregarMateriaPrima(lMpAsignadas, palets.getSelectionModel().getSelectedItem(), productoId, productoCodigo, productoDesc);
            } catch (IOException | InterruptedException e) { throw new RuntimeException(e); }
        });

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(bAtras, bAgregar);
        root.setBottom(hBox);
    }

    private void mostrasAgregadas(VBox vBox, List<String> lMpAsignadas) {
        for(String mpAsignada : lMpAsignadas) {
            vBox.getChildren().add(new Label(mpAsignada));
        }
    }

    private void agregarMateriaPrima(
            List<String> mpAsignadas,
            PaletMezclaDto item,
            Long id,
            String codigo,
            String desc) throws IOException, InterruptedException {
        mpAsignadas.add(item.productoCodigo() + "  - " + item.productoDesc() + " - " + item.loteContenido());
        seleccionarProducto(id, codigo, desc);
    }
}