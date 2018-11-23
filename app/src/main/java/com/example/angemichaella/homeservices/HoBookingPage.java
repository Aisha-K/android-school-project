package com.example.angemichaella.homeservices;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HoBookingPage extends AppCompatActivity {

    ServiceProvider sp;
    DatabaseReference spNode;

    DatePickerDialog datePicker;
    ArrayList<String> times = new ArrayList<>();
    ArrayList<String> days = new ArrayList<>();
    Day selectedDay;

    Spinner daySpinner;
    Spinner timeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_booking_page);
        spNode = FirebaseDatabase.getInstance().getReference("users").child("-LRTSHm6pPlC2iGK5UCx");// un service pw provider
        setServiceProvider();
        selectedDay = null;


        daySpinner = findViewById(R.id.dateSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
    }


    public void setTimesList(Day day){
        times.clear();
        for(Availability a: sp.availabilities){
            if(a.getDay() == day){
                times.add(a.getFrom().toString()+" to "+a.getTo().toString());
            }
        }
    }

    public void setDaySpinner(){
        ArrayAdapter<String> dayAdptr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days);
        daySpinner.setAdapter(dayAdptr);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {
                String dayString = (String) parent.getItemAtPosition(pos);// your code here
                setTimesList(stringToDay(dayString));
                setTimesSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void setTimesSpinner(){
        ArrayAdapter<String> timeAdptr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, times);
        timeSpinner.setAdapter(timeAdptr);
    }

    public void setDaysList(){
        for(Availability a: sp.availabilities){
            if(days.indexOf(a.getDay()) < 0){
                days.add(a.getDay().toString());
            }
        }
    }

    private void setServiceProvider(){

        spNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sp = dataSnapshot.getValue(ServiceProvider.class);
                setDaysList();
                setDaySpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private Day stringToDay(String day){
        if (day.equals("MONDAY")){
            return Day.MONDAY;
        }
        else if (day.equals("TUESDAY")){
            return Day.TUESDAY;
        }
        else if (day.equals("WEDNESDAY")){
            return Day.WEDNESDAY;
        }
        else if (day.equals("THURSDAY")){
            return Day.THURSDAY;
        }
        else if (day.equals("FRIDAY")){
            return Day.FRIDAY;
        }
        else if (day.equals("SATURDAY")){
            return Day.SATURDAY;
        }
        else if (day.equals("SUNDAY")){
            return Day.SUNDAY;
        }
        return null;
    }
}
