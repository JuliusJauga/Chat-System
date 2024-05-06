module com.example.chatroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.chatroom to javafx.fxml;
    exports com.example.chatroom;
    exports com.example.chatroom.client;
    opens com.example.chatroom.client to javafx.fxml;
    exports com.example.chatroom.server;
    opens com.example.chatroom.server to javafx.fxml;
}