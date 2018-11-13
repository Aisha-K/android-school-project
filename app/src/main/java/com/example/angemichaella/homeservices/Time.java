package com.example.angemichaella.homeservices;

public class Time {

    public static final int AM = 0;
    public static final int PM = 1;


    private int hour;
    private int minute;
    private int time; //in minutes after midnight

    public Time(int hr, int min, int tense){
        hour = hr;
        minute = min;
        hour += 12*tense;
        if(hour == 24){
            hour = 0;
        }
        time = 60*hour + minute;

    }

    public int time(){
        return time;
    }

    public String toString(){//RETURNS TIME IN 24 HR FORMAT
        String res = hour+":";
        if(minute <10){
            res+="0"+minute;
        }else{
            res += minute;
        }
        return res;


        //BELOW FOR IF U WANNA SEE THE TIME IN 12 HR FORMAT
        /*
                int adjustedHour = hour%12;
        if(hour == 12){
            adjustedHour = 12;
        }
        int tense = hour/12;

        String res = adjustedHour+":";
        if(minute <10){
            res+="0"+minute;
        }else{
            res += minute;
        }

        if(tense == AM){
            res += " am";
        }else if(tense == PM){
            res += " pm";
        }
         */

    }



}
