package com.example.angemichaella.homeservices;

public class HomeOwner extends User {

    //default constructor
    public HomeOwner(){
        super();
    }

    public HomeOwner(String userName, String password, String userId){
        super(userName, password, userId);
        type="HomeOwner";
    }

    public HomeOwner(String userName, String password, String userId, String type){
        super(userName, password, userId, type);
    }
}
