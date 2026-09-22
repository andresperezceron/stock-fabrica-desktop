package com.fabrica.stock.desktop.mezcla.ui;

import com.fabrica.stock.desktop.mezcla.api.MezclaDto;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MezclaComp {

    public static Label labelFecha(LocalDateTime fecha) {
        Label lbFechaCreacion = new Label(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
        lbFechaCreacion.setFont(Font.font(13));
        return lbFechaCreacion;
    }

    public static Label labelProducto(MezclaDto mezcla) {
        Label lbProducto = new Label(mezcla.productoCodigo() + "   " +  mezcla.productoDesc());
        lbProducto.setFont(Font.font(15));
        return lbProducto;
    }

    public static Label labelComposicion() {
        Label lbComposicion = new Label("Composición:");
        lbComposicion.setFont(Font.font(13));
        lbComposicion.setTextFill(Color.DARKBLUE);
        return lbComposicion;
    }

    public static VBox vBoxBotton() {
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
}