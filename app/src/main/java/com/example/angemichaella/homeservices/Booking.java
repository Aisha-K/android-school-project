package com.example.angemichaella.homeservices;

import android.app.DownloadManager;
import android.widget.Toast;

import java.util.Date;


public class Booking {


    private String bookingId;

    private String homeOwnerName;
    private String serviceProviderName;
    private String serviceProviderId;
    private String serviceName;
    private double rating;
    private String comment;
    private String date;
    private Availability availability;
    private boolean rated;

    public Booking(){
        //blank constructor
    }

    public Booking(String hoName, String spName, String spId, String srvName, Availability a, String d, String bkId){
        homeOwnerName = hoName;
        serviceProviderName = spName;
        serviceProviderId = spId;
        serviceName =srvName;
        availability = a;
        date= d;
        bookingId = bkId;
        rated = false;
        rating = -1;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setRating(double rating) {
        this.rating = rating;
        rated = true;
    }

    public void setComment(String msg){ comment = msg;}

    public String getBookingId() {
        return bookingId;
    }

    public boolean isRated() {
        return rated;
    }

    public String getHomeOwnerName() {
        return homeOwnerName;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDate() {
        return date;
    }

    public Availability getAvailability() {
        return availability;
    }

    public double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String toString(){
        String result = "Booking for " + homeOwnerName+ " on "+ date + " provided by "+serviceProviderName;
        return result;
    }


}
