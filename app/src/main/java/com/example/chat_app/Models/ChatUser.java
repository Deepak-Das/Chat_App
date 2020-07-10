package com.example.chat_app.Models;

public class ChatUser {
    private String id,user_name,image_URL,status,last_message;
    int last_message_count;

    public ChatUser(){}

    public ChatUser(String id, String user_name, String image_URL, String status, String last_message, int last_message_count) {
        this.id = id;
        this.user_name = user_name;
        this.image_URL = image_URL;
        this.status = status;
        this.last_message = last_message;
        this.last_message_count = last_message_count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public int getLast_message_count() {
        return last_message_count;
    }

    public void setLast_message_count(int last_message_count) {
        this.last_message_count = last_message_count;
    }

}
