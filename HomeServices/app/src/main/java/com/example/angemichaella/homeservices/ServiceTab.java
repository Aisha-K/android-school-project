package com.example.angemichaella.homeservices;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ServiceTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *  factory method to
 * create an instance of this fragment.
 */
public class ServiceTab extends Fragment {

    private Button addServiceBtn;
    private ArrayList<Service> services;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_tab, container, false);
        addServiceBtn = (Button)view.findViewById(R.id.addServiceBtn);

    //Example Case For Now
        Service s1 = new Service("Painting", false, 23.478, "a1");
        Service s2 = new Service("Lawn Mowing", true, 50, "b2");
        Service s3 = new Service("Furniture Moving", false, 12.34, "c3");

        s1.iconName = "paint_icon";
        s2.iconName = "grass_icon";
        s3.iconName = "couch_icon";

        services = new ArrayList<Service>();
        services.add(s1);
        services.add(s2);
        services.add(s3);
    //end of example case

        ServiceAdapter adtr = new ServiceAdapter(getActivity(), services);
        ListView serviceListView = (ListView)view.findViewById(R.id.serviceListView);
        serviceListView.setAdapter(adtr);


        //example of when a service is clicked
        serviceListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                        String srvName = String.valueOf(((Service)parent.getItemAtPosition(pos)).name());
                        Toast.makeText(getActivity(), srvName, Toast.LENGTH_LONG).show();
                    }
                }
        );

        return view;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
