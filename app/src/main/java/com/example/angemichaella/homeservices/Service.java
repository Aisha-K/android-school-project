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
        this.rate=rate;
        this.serviceId=serviceId;

        roundRate();

        if(isOutdoor){
            type="outdoor";
            iconName = "outdoor_icon";
        }
        else {
            type = "indoor";
            iconName = "indoor_icon";
        }

    }

    private void roundRate(){
        rate = rate*100;

        double fract = rate - (int)rate;
        if(fract >=0.5){
            rate+= 1-fract;
        }
        int roundedRate = (int)rate;
        rate = roundedRate/100.00;
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

    public String id(){
        return serviceId;
    }

    //setters
    public void setName(String name){serviceName = name;}

    public void setRate(Double newRate){rate = newRate;}

    public void setType(String type){this.type = type;}




}
