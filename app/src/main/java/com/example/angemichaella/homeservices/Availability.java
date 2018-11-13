package com.example.angemichaella.homeservices;

public class Availability {
    Day day;
    Time from;
    Time to;


    public Availability(Day d, Time f, Time t){
        day = d;
        from = f;
        to = t;
    }

    public String toString(){
        String result = day + " from " +from+" to "+to;
        return result;
    }



}
