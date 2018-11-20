package com.example.angemichaella.homeservices;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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
    DatabaseReference spServices;
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
        spServices = FirebaseDatabase.getInstance().getReference("Service Provider's Services");

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

        //if user holds service to add
        myServiceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v, int index, long arg3) {


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("Add Service");
                final Service clickedServ = (Service)myServiceListView.getItemAtPosition(index);
                builder.setMessage("Are you sure you want to add the " + '"'+ clickedServ.name()+ '"' +" service?");


                builder.setPositiveButton("ADD", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){

                        addService(clickedServ.id());
                        dialog.dismiss();//user clicked create

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });


        return d;
    }


    //adds a service to database for Service Provider's database
    private void addService(String ServiceId)
    {
       
        Toast.makeText(getActivity(), "Service Added", Toast.LENGTH_LONG).show();
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
