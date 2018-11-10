package com.example.angemichaella.homeservices;

public class ServiceProvider extends User {

    //default constructor
    public ServiceProvider(){
        super();
    }

    // to be completed in future project deliverable
    public ServiceProvider(String userName, String password, String userId){
        super(userName, password, userId);
        type="ServiceProvider";
    }

    public ServiceProvider(String userName, String password, String userId, String type){
        super(userName, password, userId, type);
    }
}
