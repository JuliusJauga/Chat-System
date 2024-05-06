package com.example.chatroom.client;

import com.example.chatroom.Application;
import com.example.chatroom.Message;
import com.example.chatroom.Room;
import com.example.chatroom.server.HostController;
import com.example.chatroom.server.UserThread;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MakeRoomController implements Initializable {
    ConnectionThread newThread;
    public MakeRoomController(ConnectionThread newThread) {
        this.newThread = newThread;
    }
    @FXML
    private ListView<String> in_users;

    @FXML
    private ListView<String> out_users;

    private ObservableList<String> out_users_list;
    private ObservableList<String> in_users_list;
    private List<UserThread> roomsUsers;
    public void initialize(URL url, ResourceBundle rb) {
        roomsUsers = new ArrayList<>();
        out_users_list = FXCollections.observableArrayList();
        in_users_list = FXCollections.observableArrayList();
        for (UserThread user : HostController.threads) {
            out_users_list.add(user.clientName);
        }
        out_users.setItems(out_users_list);
        in_users.setItems(in_users_list);
        out_users.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
                String selectedPerson = out_users.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    in_users_list.add(selectedPerson);
                    for (UserThread user : HostController.threads) {
                        if (user.clientName.equals(selectedPerson)) {
                            roomsUsers.add(user);
                            break;
                        }
                    }
                }
                out_users_list.remove(selectedPerson);
            }
        });
        in_users.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
                String selectedPerson = in_users.getSelectionModel().getSelectedItem();
                in_users_list.remove(selectedPerson);
                out_users_list.add(selectedPerson);
                for (UserThread user : HostController.threads) {
                    if (user.clientName.equals(selectedPerson)) {
                        roomsUsers.remove(user);
                        break;
                    }
                }
            }
        });
    }
    @FXML
    public void make_room(ActionEvent event) throws IOException {
        Message message = new Message(null, null);
        List<String> temp = new ArrayList<>(in_users_list);
        Room newRoom = new Room(temp, newThread.username + "'s room");
        message.setUpdateRooms(true);
        message.setRoomUsers(newRoom);
        newThread.sendMessage(message);
        HostController.rooms.add(newRoom);
        try {
            FileOutputStream fileOut = new FileOutputStream("rooms.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(HostController.rooms);
            out.close();
            fileOut.close();
        } catch (IOException e) {
        }
        Platform.exit();
    }
}
