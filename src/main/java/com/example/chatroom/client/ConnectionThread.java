package com.example.chatroom.client;

import com.example.chatroom.Message;
import com.example.chatroom.server.HostController;
import com.example.chatroom.server.UserThread;
import javafx.application.Platform;

import com.example.chatroom.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Array;

public class ConnectionThread extends java.lang.Thread {
    int port;
    String ip;
    Socket socket;
    ChatController controller;
    String username;
    public ConnectionThread(String username, int port, String ip, ChatController controller) {
        this.username = username;
        this.port = port;
        this.ip = ip;
        this.controller = controller;
    }
    public void run() {
        socket = null;
        try {
            socket = new Socket(ip, port);
            Message message2 = new Message(username,null);
            message2.setFirstTime(true);
            sendMessage(message2);
            while (true) {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) in.readObject();
                if (message.isFirstTime()) {
                    System.out.println(message.getConnectedUsers().toString());
                    Platform.runLater(() -> controller.setUsers(message.getConnectedUsers()));
                }
                else if (message.isNewUser()) {
                    Platform.runLater(() -> controller.AddUser(message.getUsername()));
                }
                else if (message.isUpdateChat()) {
                    Platform.runLater(() -> controller.setChatData(message.getUpdateData()));
                }
                else if (message.isAddChatData()) {
                    if (message.getUsername().equals(controller.getSelectedPerson()) || message.getUsername().equals(username)) {
                        Platform.runLater(() -> controller.addChatData(message));
                    }
                }
                else if (message.isUpdateRooms()) {
                    if (message.getRoomUsers().members.contains(username)) {
                        Platform.runLater(() -> controller.addRoom(message.getRoomUsers().roomName));
                    }
                }
                else if (message.isOpenRoomChat()) {
                    if (message.getRoomUsers().members.contains(username)) {
                        Platform.runLater(() -> controller.openRoomChat(message.getRoomUsers().roomMessages));
                    }
                }
                else if (message.isMessageRoomChat() /*&& controller.getSelectedGroup() != null*/) {
                    if (message.getRoomUsers().members.contains(username)) {
                        Platform.runLater(() -> controller.addRoomChat(message));
                    }
                }
                else if (controller.getSelectedGroup() != null) {
                    Platform.runLater(() -> controller.addRoomChat(message));
                }
                else if (message.isDisconnectedThread()) {
                    Platform.runLater(() -> controller.disconnectPerson(message.getDisconnectedUserString()));
                }
            }
        } catch (Exception e) {

        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendMessage(Message message) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(message);
    }

}
