package com.example.angemichaella.homeservices;

public class Availability {
    Day day;
    Time from;
    Time to;

    public Availability(){

    }

    public Availability(Day d, Time f, Time t){
        day = d;
        from = f;
        to = t;
    }

    public String toString(){
        String result = day + " from " +from+" to "+to;
        return result;
    }

    public String day(){
        return "" + day;
    }

    public String time(){
        return "" + from + " : " + to;
    }

    public Day getDay() {
        return day;
    }

    public Time getFrom() {
        return from;
    }

    public Time getTo() {
        return to;
    }
}
