package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HoBookingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        //currently using the same fragment layout as service privder bookings tab, create new one if you need to
        View view = inflater.inflate(R.layout.fragment_sp_manage_bookings, container, false);
        return view;
    }

    }
