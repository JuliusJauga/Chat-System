package com.example.chatroom;

import com.example.chatroom.server.UserThread;

import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    public List<String> members = new ArrayList<String>();
    public String roomName;
    public List<String[]> roomMessages;
    public Room(List<String> members, String name) {
        roomMessages = Collections.synchronizedList(new ArrayList<String[]>());
        roomName = name;
        this.members = members;
    }
    public void add(String member) {
        members.add(member);
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
