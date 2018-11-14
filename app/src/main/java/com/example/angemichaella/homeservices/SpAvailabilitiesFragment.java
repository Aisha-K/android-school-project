package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SpAvailabilitiesFragment extends Fragment{

    FloatingActionButton addAvailabilityBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflates fragment layout
        View view= inflater.inflate(R.layout.fragment_sp_availabilities, container, false);

        addAvailabilityBtn= view.findViewById(R.id.floatingAddButton);

        //availability button on click listener
        addAvailabilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAvailabilityPopUp();
            }
        });


        return view;
    }

    private void newAvailabilityPopUp(){
        AddAvailabilityDialog d = new AddAvailabilityDialog();
        d.show(getActivity().getSupportFragmentManager(), "hi");
    }





}
