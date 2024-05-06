package com.example.chatroom;

import com.example.chatroom.client.JoinController;
import com.example.chatroom.server.HostController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    void host_button(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("host-view.fxml"));
        HostController newController = new HostController();
        fxmlLoader.setController(newController);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Hosting server");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void join_button(ActionEvent event) throws IOException {
        openGroupWindow();
    }
    private void openGroupWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("join-view.fxml"));
        JoinController newController = new JoinController();
        fxmlLoader.setController(newController);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Joining server");
        stage.setScene(scene);
        stage.show();
    }
}