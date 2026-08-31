package com.fabrica.stock.desktop.principal.ui;

import com.fabrica.stock.desktop.infrastructure.ApiClient;
import com.fabrica.stock.desktop.terminal.api.TerminalApi;
import com.fabrica.stock.desktop.terminal.ui.TerminalController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class PrincipalController {

    Long maquinaId = 1L;

    @FXML
    protected void abrirTerminal(ActionEvent event) throws IOException {
        ApiClient apiClient = new ApiClient();
        ObjectMapper objectMapper = new ObjectMapper();
        TerminalApi terminalApi = new TerminalApi(apiClient, objectMapper);
        TerminalController controller = new TerminalController(terminalApi, maquinaId);

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/com/fabrica/stock/desktop/terminal.fxml"
                        )
                );

        loader.setController(controller);

        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(scene);
    }
}