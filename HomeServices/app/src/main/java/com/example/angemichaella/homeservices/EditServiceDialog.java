package com.example.angemichaella.homeservices;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class EditServiceDialog extends AppCompatDialogFragment {
    EditText serviceName;
    EditText serviceRate;
    boolean isOutdoor;
    private EditServiceDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle mArgs = getArguments();
        String name = mArgs.getString("srv_name");
        String rate = mArgs.getString("srv_rate");
        final String id = mArgs.getString("srv_id");
        String type = mArgs.getString("srv_type");

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        LayoutInflater inf = getActivity().getLayoutInflater();
        View v = inf.inflate(R.layout.dialog_editservice, null);

        b.setView(v);
        b.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        serviceName = (EditText) v.findViewById(R.id.serviceNameET);
        serviceRate = (EditText) v.findViewById(R.id.serviceRateET);
        serviceName.setText(name);
        serviceRate.setText(rate);

        String svN = serviceName.toString();
        Double svR = Double.parseDouble(serviceRate.toString());

        //Determine if service is indoor or outdoor
        final RadioButton outdoor = (RadioButton) v.findViewById(R.id.btnOutdoorService);

        View.OnClickListener outdoor_listener = new View.OnClickListener()
        {
            public void onClick(View v) {
                isOutdoor = true;
            }
        };

        outdoor.setOnClickListener(outdoor_listener);

        final RadioButton indoor = (RadioButton) v.findViewById(R.id.btnIndoorService);

        View.OnClickListener indoor_listener = new View.OnClickListener()
        {
            public void onClick(View v) {
                isOutdoor = false;
            }
        };

        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (false) { //insert validation of fields here
                } else {
                    String srvName = serviceName.getText().toString();
                    double rate = Double.parseDouble(serviceRate.getText().toString());
                    listener.applyTexts(id, srvName, rate);

                }
            }
        });

        indoor.setOnClickListener(indoor_listener);

        return b.create();
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            listener = (EditServiceDialogListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement ESD Listener");
        }
    }

    public interface EditServiceDialogListener {
        void applyTexts(String id, String serviceName, double serviceRate);
    }
}
