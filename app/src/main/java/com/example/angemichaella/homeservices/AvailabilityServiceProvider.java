package com.example.angemichaella.homeservices;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import android.view.View;
import android.widget.Toast;

public class AvailabilityServiceProvider extends AppCompatActivity implements AddAvailabilityDialog.AddAvailabilityListener  {

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
    public void receiveAvailability(Time from, Time to){
        Toast.makeText(this ,"from " + from.time() + " to " +to.time() +" clock", Toast.LENGTH_LONG).show();
    }


}
