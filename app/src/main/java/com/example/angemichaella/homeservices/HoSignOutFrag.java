package com.example.angemichaella.homeservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HoSignOutFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ho_signout, container, false);


        Button SignOutBtn = view.findViewById(R.id.HoSignOutBtn);

        //clicking the sign out button
        SignOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send to main activity and finish this one
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);    //clears all previous activities
                startActivity(intent);
                //getActivity().finish();
            }
        });

        return view;


    }
}
