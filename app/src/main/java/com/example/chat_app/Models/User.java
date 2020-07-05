package com.example.chat_app.Models;

public class User {
    private String id,user_name,image_URL;

    public User(){}

    public User(String id, String user_name, String image_URL) {
        this.id = id;
        this.user_name = user_name;
        this.image_URL = image_URL;
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

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", image_URL='" + image_URL + '\'' +
                '}';
    }
}
