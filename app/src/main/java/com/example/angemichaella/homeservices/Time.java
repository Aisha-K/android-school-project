package com.example.angemichaella.homeservices;

public class Time {

    public static final int AM = 0;
    public static final int PM = 0;


    private int hour;
    private int minute;
    private int time; //in minutes after midnight

    public Time(int hr, int min, int tense){
        hour = hr;
        minute = min;
        time = 60*hour + minute;
        hour += 12*tense;
        if(hour == 24){
            hour = 0;
        }

    }

    public int time(){
        return time;
    }


}
