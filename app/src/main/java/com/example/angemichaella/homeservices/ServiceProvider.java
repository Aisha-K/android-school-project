package com.example.angemichaella.homeservices;

import java.util.ArrayList;

public class ServiceProvider extends User {

    private String phoneNumber;
    private String companyName;
    private String description;
    private boolean isLicensed;
    private boolean profileCompleted;

    private ArrayList<Availability> availabilities;

    //default constructor
    public ServiceProvider(){
        super();
    }

    // to be completed in future project deliverable
    public ServiceProvider(String userName, String password, String email, String userId){
        super(userName, password, email, userId, "ServiceProvider");
        isLicensed = false;
        profileCompleted = false;
        description = "empty";
    }

    public void setProfileInfo(String phone, String company, String desc, boolean licensing){
        phoneNumber = phone;
        companyName = company;

        if(desc != null){
            description = desc;
        }
        description = desc;
        isLicensed = licensing;
        profileCompleted = true;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLicensed() {
        return isLicensed;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public ArrayList<Availability> getAvailabilities() {
        return availabilities;
    }
}
