package com.fabrica.stock.desktop.mezcla.ui;

import com.fabrica.stock.desktop.mezcla.api.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

import java.io.IOException;

public class MezclaController {
    private final MezclaApi mezclaApi;

    @FXML
    private BorderPane root;

    @FXML
    private Button ver;

    @FXML
    private Button crear;

    public MezclaController(MezclaApi mezclaApi) {
        this.mezclaApi = mezclaApi;
    }

    @FXML
    private void initialize() throws IOException, InterruptedException {
        iniciar();
        //root.setCenter(center());
        //bottom();
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
                else setText(mezcla.productoCodigo() + " - " + mezcla.productoDesc());
            }
        });

        mezclas.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> {
                    ver.setDisable(seleccionado == null);
                }
        );

        root.setCenter(mezclas);

        crear.setOnAction(event -> {
            try { crearMezcla();
            } catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });
    }

    private StackPane top() {
        Label titulo = new Label("MEZCLAS");
        titulo.setFont(Font.font(24));
        return new StackPane(titulo);
    }

    private StackPane center() throws IOException, InterruptedException {
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
                else setText(mezcla.productoCodigo() + " - " + mezcla.productoDesc());
            }
        });

        mezclas.getSelectionModel().selectedItemProperty().addListener(
                (observable, anterior, seleccionado) -> {
                    ver.setDisable(seleccionado == null);
                }
        );

        return new StackPane(mezclas);
    }

    private void bottom() {
        crear.setOnAction(event -> {
            try { crearMezcla();
            } catch(IOException | InterruptedException e) { throw new RuntimeException(e); }
        });
    }

    private void crearMezcla() throws IOException, InterruptedException {
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

        Label titulo = new Label("CREANDO NUEVA MEZCLA");
        titulo.setFont(Font.font(24));

        root.setTop(new StackPane(titulo));
        root.setCenter(productos);
    }
}