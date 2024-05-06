package com.example.chatroom.server;

import com.example.chatroom.Message;
import com.example.chatroom.Room;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class UserThread extends Thread implements Serializable {
    public String clientName;
    Socket client;
    public static List<String> userNames = Collections.synchronizedList(new ArrayList<>());
    public UserThread(Socket client) {
        this.client = client;
    }
    public void run() {
        while(client.isConnected() && !client.isClosed()) {
            try {
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                Message message = (Message) in.readObject();
                if (message.isFirstTime()) {
                    setupUser(message);
                }
                else if (message.isUpdateChat()) {
                    List<String> names = new ArrayList<>();
                    names.add(message.getUsername());
                    names.add(message.getTargetString());
                    names.sort(String.CASE_INSENSITIVE_ORDER);
                    synchronized (HostController.userMessages) {
                        Map<String, List<Message>> firstMap = HostController.userMessages.get(names.get(0));
                        if (firstMap == null) {
                            firstMap = new HashMap<>();
                            HostController.userMessages.put(names.get(0), firstMap);
                        }
                        List<Message> messages = firstMap.get(names.get(1));
                        if (messages == null) {
                            messages = new ArrayList<>();
                            firstMap.put(names.get(1), messages);
                        }
                        Message newmessage = new Message(null, null);
                        newmessage.setUpdateChat(true);
                        newmessage.setUpdateData(messages);
                        sendMessage(newmessage);
                    }
                }
                else if (message.getTargetString() != null) {
                    List<String> names = new ArrayList<>();
                    names.add(message.getUsername());
                    names.add(message.getTargetString());
                    names.sort(String.CASE_INSENSITIVE_ORDER);
                    synchronized (HostController.userMessages) {
                        Map<String, List<Message>> firstMap = HostController.userMessages.get(names.get(0));
                        if (firstMap == null) {
                            firstMap = new HashMap<>();
                            HostController.userMessages.put(names.get(0), firstMap);
                        }
                        List<Message> messages = firstMap.get(names.get(1));
                        if (messages == null) {
                            messages = new ArrayList<>();
                            firstMap.put(names.get(1), messages);
                        }
                        messages.add(message);
                        try {
                            FileOutputStream fileOut = new FileOutputStream("userdata.ser");
                            ObjectOutputStream out = new ObjectOutputStream(fileOut);
                            out.writeObject(HostController.userMessages);
                            out.close();
                            fileOut.close();
                        } catch (IOException e) {
                        }
                        Message newmessage = new Message(message.getUsername(), message.getMessage());
                        newmessage.setTargetString(message.getTargetString());
                        newmessage.setAddChatData(true);
                        synchronized (HostController.threads) {
                            for (UserThread thread : HostController.threads) {
                                if (thread.isAlive()) {
                                    if (names.get(0).equals(thread.clientName) || names.get(1).equals(thread.clientName)) {
                                        thread.sendMessage(newmessage);
                                    }
                                }
                            }
                        }
                    }
                }
                else if (message.isUpdateRooms()) {
                    Message newmessage = new Message(null, null);
                    newmessage.setUpdateRooms(true);
                    newmessage.setRoomUsers(message.getRoomUsers());
                    //newmessage.setTargetUsers(message.getTargetUsers());
                    synchronized (HostController.threads) {
                        for (UserThread thread : HostController.threads) {
                            if (thread.isAlive()) {
                                if (message.getRoomUsers().members.contains((thread.clientName))) {
                                    thread.sendMessage(newmessage);
                                }
                            }
                        }
                    }
                }
                else if (message.isOpenRoomChat()) {
                    List<String> names = new ArrayList<>();
                    Message newmessage = new Message(message.getUsername(), message.getMessage());

                    for (Room room : HostController.rooms) {
                        if (message.getTargetRoomName().equals(room.roomName)) {
                            names = new ArrayList<>(room.members);
                            newmessage.setOpenRoomChat(true);
                            newmessage.setRoomUsers(room);
                            newmessage.setTargetRoomName(message.getTargetRoomName());
                        }
                    }
                    synchronized (HostController.threads) {
                        for (UserThread thread : HostController.threads) {
                            if (thread.isAlive()) {
                                if (names.contains((thread.clientName))) {
                                    sendMessage(newmessage);
                                }
                            }
                        }
                    }
                }
                else if (message.isMessageRoomChat() || message.getTargetRoomName() != null) {
                    List<String> names = new ArrayList<>();
                    Message newmessage = new Message(message.getUsername(), message.getMessage());

                    for (Room room : HostController.rooms) {
                        if (message.getTargetRoomName().equals(room.roomName)) {
                            names = new ArrayList<>(room.members);
                            newmessage.setMessageRoomChat(true);
                            newmessage.setRoomUsers(room);
                            newmessage.setTargetRoomName(message.getTargetRoomName());
                            String[] mes = new String[2];
                            mes[0] = message.getUsername();
                            mes[1] = message.getMessage();
                            room.roomMessages.add(mes);
                        }
                    }
                    try {
                        FileOutputStream fileOut = new FileOutputStream("rooms.ser");
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        out.writeObject(HostController.rooms);
                        out.close();
                        fileOut.close();
                    } catch (IOException e) {
                    }
                    synchronized (HostController.threads) {
                        for (UserThread thread : HostController.threads) {
                            if (thread.isAlive()) {
                                if (names.contains((thread.clientName))) {
                                    thread.sendMessage(newmessage);
                                }
                            }
                        }
                    }
                }
                else if (message.isDisconnectedThread()) {
                    Message newmessage = new Message(null, null);
                    newmessage.setDisconnectedThread(true);
                    newmessage.setDisconnectedUserString(message.getDisconnectedUserString());
                    synchronized (HostController.threads) {
                        for (UserThread thread : HostController.threads) {
                            if (thread.isAlive()) {
                                thread.sendMessage(newmessage);
                            }
                        }
                    }
                    userNames.remove(message.getDisconnectedUserString());
                    HostController.threads.removeIf(thread -> thread.isAlive() && thread.clientName.equals(message.getDisconnectedUserString()));
                }
            } catch (IOException e) {

            } catch (ClassNotFoundException e) {

            }
        }
    }
    public void setupUser(Message message) throws IOException {
        clientName = message.getUsername();
        synchronized (userNames) {
            List<String> names = new ArrayList<>(userNames);
            userNames.add(message.getUsername());
            Message newMessage = new Message(null,null);
            newMessage.setFirstTime(true);
            newMessage.setConnectedUsers(names);
            sendMessage(newMessage);
        }
        synchronized (HostController.threads) {
            Message message1 = new Message(null, null);
            message1.setNewUser(true);
            message1.setUsername(message.getUsername());
            for (UserThread thread : HostController.threads) {
                if (thread.isAlive()) {
                    thread.sendMessage(message1);
                }
            }
            for (Room room : HostController.rooms) {
                for (String name : room.members) {
                    if (name.equals(clientName)) {
                        Message newmes = new Message(null, null);
                        newmes.setUpdateRooms(true);
                        newmes.setRoomUsers(room);
                        newmes.setTargetRoomName(message.getTargetRoomName());
                        sendMessage(newmes);
                    }
                }
            }
        }
    }
    public void sendMessage(Message message) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        out.writeObject(message);
    }
}
