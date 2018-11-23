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
        return "" + from + " to " + to;
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

    public boolean equals(Availability other){
        return (day == other.getDay() && from.equals(other.getFrom()) && to.equals(other.getTo()));
    }


    /**
     * This method returns true if two availabilities are over lapping (on the same day, and a region of shared time)
     * @param other
     * @return
     */
    public boolean overlaps(Availability other){
        if(day != other.getDay()){
            return false;
        }else{
            Availability earlierAvl;
            Availability laterAvl;
            if(from.compareTo(other.getFrom()) <=0){
                earlierAvl = this;
                laterAvl = other;
            }else{
                earlierAvl = other;
                laterAvl = this;
            }

            return (earlierAvl.getTo().compareTo(laterAvl.getFrom()) >= 0);
        }

    }

    /**
     * returns a super Availability that is this one merged with another availability, assuming they are overlapping
     * @param other
     * @return
     */
    public Availability mergeWith(Availability other){
        Time earliestFrom;
        Time latestTo;
        if(from.compareTo(other.getFrom()) <0){
            earliestFrom = from;
        }else{
            earliestFrom = other.getFrom();
        }

        if(to.compareTo(other.getTo()) >0){
            latestTo =to;
        }else{
            latestTo = other.getTo();
        }

        return new Availability(day, earliestFrom, latestTo);
    }


    /*
    method testing whether avl 'other' is contained in this avl.
     */
    public boolean contains(Availability other){

        if(other.getDay() != day){//if not on the same day
            return false;
        }

        if(from.compareTo(other.getFrom())>0){ //if this avl starts after the other
            return false;
        }

        if(to.compareTo(other.getTo())<0){/// if this avl ends before the other
            return false;
        }

        return true;
    }
}
