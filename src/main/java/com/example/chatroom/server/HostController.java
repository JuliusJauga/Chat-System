package com.example.chatroom.server;

import com.example.chatroom.Message;
import com.example.chatroom.Room;
import com.example.chatroom.client.ChatController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class HostController {
    @FXML
    private TextField port_field;

    public static List<UserThread> threads;
    public static List<Room> rooms;
    public static Map<String, Map<String, List<Message>>> userMessages;

    @FXML
    void host_click(ActionEvent event) throws IOException {
        threads = Collections.synchronizedList(new ArrayList<UserThread>());
        rooms = Collections.synchronizedList(new ArrayList<>());
        try {
            FileInputStream fileIn = new FileInputStream("rooms.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            List<Room> deserializedRooms = (List<Room>) in.readObject();
            in.close();
            fileIn.close();
            rooms.addAll(deserializedRooms);
            in.close();
            fileIn.close();
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from file: " + e.getMessage());
        }
        userMessages = Collections.synchronizedMap(new HashMap<String, Map<String, List<Message>>>());
        try {
            FileInputStream fileIn = new FileInputStream("userdata.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            userMessages = (Map<String, Map<String, List<Message>>>) in.readObject();
            in.close();
            fileIn.close();
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading object from file: " + e.getMessage());
        }
        new Thread(() -> {
            ServerSocket socket = null;
            try {
                socket = new ServerSocket(Integer.parseInt(port_field.getText()));
                while (true) {
                    try {
                        Socket client = socket.accept();
                        UserThread new_user = new UserThread(client);
                        synchronized (threads) {
                            threads.add(new_user);
                        }
                        new_user.start();
                    } catch (SocketException e) {
                        System.out.println("Socket closed.");
                    } catch (IOException e) {
                        System.err.println("Failed to accept connection.");
                    } catch (Exception e) {
                        System.err.println("Unknown error.");
                    }
                }
            } catch (IOException e) {

            }
            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
