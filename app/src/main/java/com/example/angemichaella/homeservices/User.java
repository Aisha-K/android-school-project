package com.example.angemichaella.homeservices;

//IF REMOVING/MODIFYING ATTRIBUTES CHECK IF FIREBASE STILL WORKS
//firebase relies on some methods/ syntax to store/retrieve objects properly
public class User {

    private String username;
    private String password;
    private String userId;
    private String type;
    private String email;


    //default constructor
    public User(){
    }

    public User(String userName, String password, String email, String userId, String type) {
        this.username = userName;
        this.password = password;
        this.userId = userId;
        this.email = email;
        this.type=type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
