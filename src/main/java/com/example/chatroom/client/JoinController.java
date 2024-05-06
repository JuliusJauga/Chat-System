package com.example.chatroom.client;

import com.example.chatroom.Application;
import com.example.chatroom.server.HostController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class JoinController {
    @FXML
    private TextField ip_address_field;

    @FXML
    private TextField port_field;

    @FXML
    private TextField user_name_field;

    @FXML
    void join_click(ActionEvent event) throws IOException {
        String ip_address;
        int port;
        String username;
        /*try {
            ip_address = ip_address_field.getText();
            port = Integer.parseInt(port_field.getText());
            username = user_name_field.getText();
        } catch (Exception e) {
            return;
        }*/
        username = user_name_field.getText();
        openGroupWindow(username, "localhost", 2345);
    }
    private void openGroupWindow(String userName, String ip_address, int port) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("chat.fxml"));
        ChatController newController = new ChatController(userName, ip_address, port);
        fxmlLoader.setController(newController);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        newController.setStage(stage);
        stage.setTitle(userName + "'s window");
        stage.setScene(scene);
        stage.show();
    }
}
