package com.example.angemichaella.homeservices;

public class Service {
    public String serviceName;
    public String type;
    public double rate;
    public String serviceId;   //database key
    public String iconName;


    //default constructor
    public Service(){
        iconName = "default_icon";
    }

    Service(String serviceName, boolean isOutdoor, double rate, String serviceId ){
        this.serviceName=serviceName;
        this.type=type;
        this.rate=rate;
        this.serviceId=serviceId;

        if(isOutdoor){
            type="outdoor";
        }
        else {
            type = "indoor";
        }
        iconName = "default_icon";
    }

    //getters
    public double rate(){
        return rate;
    }

    public String name(){
        return serviceName;

    }

    public String type(){
        return type;

    }



}
