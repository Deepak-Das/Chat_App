package com.example.chat_app.Models;

public class User {
    private String id,user_name,image_URL,status;

    public User(){}

    public User(String id, String user_name, String image_URL, String status) {
        this.id = id;
        this.user_name = user_name;
        this.image_URL = image_URL;
        this.status = status;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", image_URL='" + image_URL + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
