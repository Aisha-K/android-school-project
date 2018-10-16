package com.example.angemichaella.homeservices;

public class User {

    public String username;
    public String password;
    public String userId;
    public String type;

    //default constructor
    public User(){
    }

    public User(String userName, String password, String userId) {
        this.username = userName;
        this.password = password;
        this.userId = userId;
    }

    public User(String userName, String password, String userId, String type) {
        this.username = userName;
        this.password = password;
        this.userId = userId;
        this.type=type;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
