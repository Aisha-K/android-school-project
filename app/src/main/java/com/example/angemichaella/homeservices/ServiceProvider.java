package com.example.angemichaella.homeservices;

import java.util.ArrayList;

public class ServiceProvider extends User {

    private String address = "";
    private String phoneNumber = "";
    private String companyName = "";
    private String description = "";
    private boolean isLicensed = false;
    private ArrayList<Availability> availabilities;

    //default constructor
    public ServiceProvider(){
        super();
    }

    // to be completed in future project deliverable
    public ServiceProvider(String userName, String password, String email, String userId){
        super(userName, password, email, userId, "ServiceProvider");
    }

    // getters methods
    public String getAddress(){
        return address;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public String getCompanyName(){
        return companyName;
    }
    public String getDescription(){
        return description;
    }

    public boolean isLicensed() {
        return isLicensed;
    }

    // setters methods

    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLicensed(boolean licensed) {
        isLicensed = licensed;
    }
}
