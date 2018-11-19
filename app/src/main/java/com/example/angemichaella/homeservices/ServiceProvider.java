package com.example.angemichaella.homeservices;

import java.util.ArrayList;

public class ServiceProvider extends User {

    private String phoneNumber;
    private String companyName;
    private String description;
    private String address;
    private boolean licensed;
    private boolean profileCompleted;
    private boolean hasAvailabities = false;

    private ArrayList<Availability> availabilities = new ArrayList<Availability>();

    //default constructor
    public ServiceProvider(){
        super();
    }

    // to be completed in future project deliverable
    public ServiceProvider(String userName, String password, String email, String userId){
        super(userName, password, email, userId, "ServiceProvider");
        licensed = false;
        profileCompleted = false;
        description = "empty";

    }

    public void setProfileInfo(String phone, String company, String address, String desc, boolean licensing){
        phoneNumber = phone;
        companyName = company;
        this.address = address;

        if(desc != null){
            description = desc;
        }
        description = desc;
        licensed = licensing;
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
        return licensed;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }

    public ArrayList<Availability> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(ArrayList<Availability> availabilities){
        this.availabilities = availabilities;
    }

    public void addAvailability(Availability availability){
        availabilities.add(availability);

        if(!hasAvailabities){
            hasAvailabities = true;
        }
    }

    public void removeAvailabitiy(Availability availability){
        availabilities.remove(availability);

        if(availabilities.isEmpty()){
            hasAvailabities = false;
        }
    }

    public boolean avAlreadyExists(Availability av){
        return availabilities.contains(av);
    }

    public boolean hasAvailabilities(){
        return this.hasAvailabities;
    }

    public String getAddress() {
        return address;
    }

    public boolean isHasAvailabities() {
        return hasAvailabities;
    }
}
