package com.example.angemichaella.homeservices;

import java.io.Serializable;

public class Time implements Serializable {

    public static final int AM = 0;
    public static final int PM = 1;


    private int hour;
    private int minute;
    private int tense; //in minutes after midnight

    public Time(){

    }

    public Time(int hr, int min, int tns){
        hour = hr;
        minute = min;
        if(hour == 12){
            hour = 0;
        }
        tense = tns;

    }

    public int time(){
        return 60*(hour+12*tense) + minute;
    }

    public String toString(){//RETURNS TIME IN 12 HR FORMAT
        String res = hour+":";
        if(minute <10){
            res+="0"+minute;
        }else{
            res += minute;
        }

        if(tense == AM){
            res+=" AM";
        }else if(tense == PM){
            res+=" PM";
        }
        return res;

    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getTense() {
        return tense;
    }

    public boolean equals(Time otherTime) {
        return this.time() == otherTime.time();
    }

    public int compareTo(Time t){
        return this.time() - t.time();
    }
}