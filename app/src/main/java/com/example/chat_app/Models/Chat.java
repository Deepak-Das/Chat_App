package com.example.chat_app.Models;

public class Chat {
    String sender_id,receiver_id,message;

    public Chat(String sender_id, String receiver_id, String message) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
    }

    public Chat() {
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
