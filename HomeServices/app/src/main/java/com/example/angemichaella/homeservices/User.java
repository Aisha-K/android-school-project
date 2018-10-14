package com.example.angemichaella.homeservices;

public abstract class User {

    public String username;
    public String password;
    public String userId;
    private int type;

    User(String userName, String password, String userId) {
        this.username = userName;
        this.password = password;
        this.userId = userId;
    }

}
