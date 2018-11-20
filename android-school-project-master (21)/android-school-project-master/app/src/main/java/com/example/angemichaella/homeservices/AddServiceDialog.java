package com.example.angemichaella.homeservices;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddServiceDialog extends AppCompatDialogFragment {

    protected ArrayList<Service> services;
    DatabaseReference databaseServices;
    ListView myServiceListView;

    EditText serviceName;
    EditText serviceRate;
    TextView dialogTitle;
    Button okbtn;
    Button cancelbtn;

    private AddServiceDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        LayoutInflater inf = getActivity().getLayoutInflater();
        View v = inf.inflate(R.layout.dialog_sp_addservice, null);

        b.setView(v);

        //intialize buttons
        cancelbtn = (Button)v.findViewById(R.id.btnCancel);
        okbtn = (Button)v.findViewById(R.id.btnOk);

        //list of available services
        myServiceListView = (ListView)v.findViewById(R.id.myServiceListView);

        services = new ArrayList<Service>();

        databaseServices = FirebaseDatabase.getInstance().getReference("Services");

        Query query = databaseServices.orderByChild("serviceName"); //orders list alphabetically based on the service name

        query.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                services.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service currService = postSnapshot.getValue(Service.class); //retrieving child node

                    services.add(currService);                          //adding service from database to list

                }
                ServiceAdapter adtr = new ServiceAdapter(getActivity(), services);
                myServiceListView.setAdapter(adtr);
            }
            public void onCancelled(DatabaseError databaseError){
            }
        });

        final AlertDialog d = b.create();

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });


        return d;
    }



    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        /**
         try {
         listener = (AddServiceDialog.AddServiceDialogListener) context;
         } catch (ClassCastException e)
         {
         throw new ClassCastException(context.toString() + "must implement ESD Listener");
         }

         **/
    }

    public interface AddServiceDialogListener {
        void receiveServiceUpdate(String id);
    }
}
