package com.example.angemichaella.homeservices;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
    ServiceAdapter adtr;


    Button okbtn;
    Button cancelbtn;
    int clickedPos;


    private AddServiceDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        LayoutInflater inf = getActivity().getLayoutInflater();
        View v = inf.inflate(R.layout.dialog_sp_addservice, null);
        b.setView(v);
        clickedPos = -1;//nothing is chosen yet

        //initializing the initial view of the dialog
        okbtn = (Button) v.findViewById(R.id.btnOk);
        cancelbtn = (Button) v.findViewById(R.id.btnCancel);

        //initializing list form database
        services = new ArrayList<Service>();
        databaseServices = FirebaseDatabase.getInstance().getReference("Services");
        myServiceListView = (ListView) v.findViewById(R.id.myServiceListView);


        //updates list of services when data changed
        Query query = databaseServices.orderByChild("serviceName"); //orders list alphabetically based on the servie name
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                services.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Service currService = postSnapshot.getValue(Service.class);
                    services.add(currService);
                }

                adtr = new ServiceAdapter(getActivity(), services);
                myServiceListView.setAdapter(adtr);

            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myServiceListView.setOnItemClickListener(//changes clicked Pos everytime an tem gets clicked!! might need chsnging so that the chosen item stays highlighted or smth
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id){
                        clickedPos = pos;


                    }
                }
        );


        final AlertDialog test = b.create();
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedPos != -1){
                    listener.addToMyServices(services.get(clickedPos));
                }
                test.dismiss();
            }

        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                test.dismiss();
            }
        });
        return test;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            listener = (AddServiceDialog.AddServiceDialogListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement ESD Listener");
        }
    }

    public interface AddServiceDialogListener {
        void addToMyServices(Service s);
    }
}
