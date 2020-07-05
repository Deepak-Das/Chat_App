package com.example.chat_app.Models;

public class ChatUser {

    String user_id;

    public ChatUser(String user_id) {
        this.user_id = user_id;
    }

    public ChatUser() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
