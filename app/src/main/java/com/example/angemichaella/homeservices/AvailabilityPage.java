package com.example.angemichaella.homeservices;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityPage extends AppCompatActivity implements AddAvailabilityDialog.AddAvailabilityListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.availability_screen);
    }

    public void onClickABtn(View view){
        AddAvailabilityDialog d = new AddAvailabilityDialog();
        d.show(getSupportFragmentManager(), "hi");
    }

    @Override
    /**
     * heres where you can do what needs to be done by the availabilities that have been added
     */
    public void receiveAvailability(List<Day> days, Time from, Time to){

        ArrayList<Availability> avls = new ArrayList<>(); // holds the availabilities chosen from the dialog in a list

        String avlsString = "Adding Availabilities:";//for testing toast can be removed
        for(int i = 0; i< days.size(); i++){
            avls.add(new Availability(days.get(i), from, to)); //adding each availability

            avlsString += "\n" + avls.get(i); //just for testing toast can be removed
        }

        /*

        MAKE NECESSARY UPDATES TO THE SERVICE PROVIDERS INFO HERE

        */

        Toast.makeText(this ,avlsString, Toast.LENGTH_LONG).show(); //can be removed, just for display for now
    }


}
