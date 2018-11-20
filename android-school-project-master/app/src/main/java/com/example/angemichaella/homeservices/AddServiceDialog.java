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

        final AlertDialog d = b.create();

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
