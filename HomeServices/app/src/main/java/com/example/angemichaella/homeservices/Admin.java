package com.example.angemichaella.homeservices;

public class Admin extends User {
    // to be completed in future project deliverable

    //default constructor
    public Admin(){
        super();
    }

    public Admin(String userName, String password, String userId){
            super(userName, password, userId);
            type= "Admin";
    }

    public Admin(String userName, String password, String userId, String type){
        super(userName, password, userId, type);
    }
}
