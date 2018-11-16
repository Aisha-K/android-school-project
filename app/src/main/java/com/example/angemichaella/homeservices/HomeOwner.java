package com.example.angemichaella.homeservices;

public class HomeOwner extends User {

    //default constructor
    public HomeOwner(){
        super();
    }

    public HomeOwner(String userName, String password, String email, String userId){
        super(userName, password, email, userId, "HomeOwner");
    }

}
