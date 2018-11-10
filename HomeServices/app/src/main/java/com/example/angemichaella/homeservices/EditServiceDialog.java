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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class EditServiceDialog extends AppCompatDialogFragment {
    EditText serviceName;
    EditText serviceRate;
    TextView dialogTitle;
    Button okbtn;
    Button cancelbtn;

    RadioGroup servType;


    private EditServiceDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle mArgs = getArguments();

        final String title = mArgs.getString("dialog_title");
        String name = mArgs.getString("srv_name");
        String rate = mArgs.getString("srv_rate");
        final String id = mArgs.getString("srv_id");
        String type = mArgs.getString("srv_type");

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        LayoutInflater inf = getActivity().getLayoutInflater();
        View v = inf.inflate(R.layout.dialog_editservice, null);

        b.setView(v);

        //initializing the initial view of the dialog
        dialogTitle = (TextView) v.findViewById(R.id.dialogTitleTV);
        serviceName = (EditText) v.findViewById(R.id.serviceNameET);
        serviceRate = (EditText) v.findViewById(R.id.serviceRateET);
        okbtn = (Button) v.findViewById(R.id.btnOk);
        cancelbtn = (Button) v.findViewById(R.id.btnCancel);

        dialogTitle.setText(title);
        serviceName.setText(name);
        serviceRate.setText(rate);
        servType = (RadioGroup) v.findViewById(R.id.radioTypeGroup);

        if(type.equals("indoor")){
            servType.check(R.id.btnIndoorService);
        }else{
            servType.check(R.id.btnOutdoorService);
        }




        final AlertDialog test = b.create();
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{//validating null fields
                    if (false) { //insert validation of fields here
                    } else {

                        boolean isOutdoor;

                        if (servType.getCheckedRadioButtonId() == R.id.btnIndoorService){
                            isOutdoor = false;
                        }else{
                            isOutdoor = true;
                        }

                        String srvName = serviceName.getText().toString();
                        double rate = Double.parseDouble(serviceRate.getText().toString());

                        listener.receiveServiceUpdate(id, srvName, rate, isOutdoor);
                        test.dismiss();


                    }
                }catch(Exception e){
                    Toast.makeText(getActivity() ,"Please enter valid name and rate", Toast.LENGTH_LONG).show();
                }

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
            listener = (EditServiceDialogListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement ESD Listener");
        }
    }

    public interface EditServiceDialogListener {
        void receiveServiceUpdate(String id, String serviceName, double serviceRate, boolean isOutdoor);
    }
}
