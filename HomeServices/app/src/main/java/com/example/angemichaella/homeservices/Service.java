package com.example.angemichaella.homeservices;

public class Service {
    public String serviceName;
    public String type;
    public double rate;
    public String serviceId;   //database key


    //default constructor
    public Service(){
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
    }


}
