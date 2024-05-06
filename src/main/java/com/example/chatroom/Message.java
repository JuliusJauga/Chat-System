package com.example.chatroom;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private boolean firstTime;
    private boolean newUser;
    private boolean updateChat;
    private boolean updateRooms;
    private boolean updateRoomChat;
    private boolean addChatData;
    private boolean openRoomChat;
    private boolean messageRoomChat;
    private boolean disconnectedThread;
    private String targetGroupString;
    private String TargetRoomName;
    private String username;
    private String message;
    private String targetString;
    private String disconnectedUserString;
    private List<String> targetUsers;
    private List<String> connectedUsers;
    private List<Message> updateData;
    private Room roomUsers;
    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTargetRoomName() {
        return TargetRoomName;
    }

    public void setTargetRoomName(String targetRoomName) {
        TargetRoomName = targetRoomName;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public List<String> getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(List<String> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public boolean isUpdateChat() {
        return updateChat;
    }

    public void setUpdateChat(boolean updateChat) {
        this.updateChat = updateChat;
    }

    public String getTargetString() {
        return targetString;
    }

    public void setTargetString(String targetString) {
        this.targetString = targetString;
    }

    public List<Message> getUpdateData() {
        return updateData;
    }

    public void setUpdateData(List<Message> updateData) {
        this.updateData = updateData;
    }

    public boolean isAddChatData() {
        return addChatData;
    }

    public void setAddChatData(boolean addChatData) {
        this.addChatData = addChatData;
    }

    public boolean isUpdateRoomChat() {
        return updateRoomChat;
    }

    public void setUpdateRoomChat(boolean updateRoomChat) {
        this.updateRoomChat = updateRoomChat;
    }

    public boolean isUpdateRooms() {
        return updateRooms;
    }

    public void setUpdateRooms(boolean updateRooms) {
        this.updateRooms = updateRooms;
    }

    public List<String> getTargetUsers() {
        return targetUsers;
    }

    public void setTargetUsers(List<String> targetUsers) {
        this.targetUsers = targetUsers;
    }

    public boolean isOpenRoomChat() {
        return openRoomChat;
    }

    public void setOpenRoomChat(boolean openRoomChat) {
        this.openRoomChat = openRoomChat;
    }

    public String getTargetGroupString() {
        return targetGroupString;
    }

    public void setTargetGroupString(String targetGroupString) {
        this.targetGroupString = targetGroupString;
    }

    public Room getRoomUsers() {
        return roomUsers;
    }

    public void setRoomUsers(Room roomUsers) {
        this.roomUsers = roomUsers;
    }

    public boolean isMessageRoomChat() {
        return messageRoomChat;
    }

    public void setMessageRoomChat(boolean messageRoomChat) {
        this.messageRoomChat = messageRoomChat;
    }

    public boolean isDisconnectedThread() {
        return disconnectedThread;
    }

    public void setDisconnectedThread(boolean disconnectedThread) {
        this.disconnectedThread = disconnectedThread;
    }

    public String getDisconnectedUserString() {
        return disconnectedUserString;
    }

    public void setDisconnectedUserString(String disconnectedUserString) {
        this.disconnectedUserString = disconnectedUserString;
    }
}
