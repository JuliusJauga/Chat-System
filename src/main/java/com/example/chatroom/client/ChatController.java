package com.example.chatroom.client;

import com.example.chatroom.Application;
import com.example.chatroom.Message;
import com.example.chatroom.Room;
import com.example.chatroom.server.HostController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private Stage stage = new Stage();
    @FXML
    private ListView<String> group_listview = new ListView<String>();
    private ObservableList<String> groupNames = FXCollections.observableArrayList();
    public static List<Room> rooms = new ArrayList<>();
    ObservableList<String> users;
    ObservableList<Message> logMessages;
    ObservableList<String> group_list;
    @FXML
    private ListView<String> connected_users;

    ConnectionThread newThread;
    @FXML
    private TableView<Message> chat_table;

    @FXML
    private TextField message_field;
    private String userName;
    private String message;
    private String selectedPerson;
    String selectedGroup;

    private String ip;
    private int port;
    public void initialize(URL url, ResourceBundle rb) {
        users = FXCollections.observableArrayList();
        logMessages = FXCollections.observableArrayList();
        group_list = FXCollections.observableArrayList();
        TableColumn<Message, String> usernameColumn = new TableColumn<>("Username");
        TableColumn<Message, String> messageColumn = new TableColumn<>("Message");

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        messageColumn.setCellValueFactory(new PropertyValueFactory<>("message"));

        connected_users.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
                selectedGroup = null;
                selectedPerson = connected_users.getSelectionModel().getSelectedItem();
                Message message = new Message(userName, null);
                message.setTargetString(selectedPerson);
                message.setUpdateChat(true);
                if (selectedPerson != null) {
                    try {
                        newThread.sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        group_listview.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 2) {
                selectedPerson = null;
                selectedGroup = group_listview.getSelectionModel().getSelectedItem();
                Message message = new Message(null, null);
                message.setTargetRoomName(selectedGroup);
                message.setOpenRoomChat(true);
                if (selectedGroup != null) {
                    try {
                        newThread.sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        chat_table.getColumns().add(usernameColumn);
        chat_table.getColumns().add(messageColumn);
        chat_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        connected_users.setItems(users);
        chat_table.setItems(logMessages);
        group_listview.setItems(group_list);
        newThread = new ConnectionThread(userName, port, ip, this);
        newThread.start();
    }
    public ChatController(String userName, String ip, int port) {
        this.userName = userName;
        this.ip = ip;
        this.port = port;
    }
    @FXML
    void send_message(ActionEvent event) throws IOException {
        if (selectedPerson == null && selectedGroup == null);
        else if (selectedPerson != null) {
            Message message = new Message(userName, message_field.getText());
            message.setTargetString(selectedPerson);
            newThread.sendMessage(message);
        }
        else {
            Message message = new Message(userName, message_field.getText());
            message.setTargetRoomName(selectedGroup);
            message.setMessageRoomChat(true);
            newThread.sendMessage(message);
        }
    }
    @FXML
    void make_room(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("make-room.fxml"));
        MakeRoomController newController = new MakeRoomController(newThread);
        fxmlLoader.setController(newController);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle(userName + "'s Room");
        stage.setScene(scene);
        stage.show();
    }
    public void setUsers(List<String> names) {
        users.addAll(names);
    }
    public void AddUser(String name) {
        users.add(name);
    }
    public void setChatData(List<Message> chatData) {
        logMessages.clear();
        logMessages.addAll(chatData);
    }
    public void addChatData(Message chatData) {
        logMessages.add(chatData);
    }
    public String getSelectedPerson() {
        return selectedPerson;
    }
    public void addRoom(String groupName) {
        group_list.add(groupName);
    }
    public void openRoomChat(List<String[]> chats) {
        logMessages.clear();
        for (String[] message : chats) {
            logMessages.add(new Message(message[0], message[1]));
        }
    }
    public void addRoomChat(Message chatData) {
        logMessages.add(chatData);
    }
    public String getSelectedGroup() {
        return selectedGroup;
    }
    public void disconnectPerson(String name) {
        if (Objects.equals(selectedPerson, name)) {
            logMessages.clear();
        }
        users.remove(name);
    }
    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setOnCloseRequest(event -> {
            Message message = new Message(null, null);
            System.out.println(userName + " closed the window");
            message.setDisconnectedThread(true);
            message.setDisconnectedUserString(userName);
            try {
                newThread.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
