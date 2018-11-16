package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SpProfileFragment extends Fragment {
    String username;
    String id;
    DatabaseReference spNode; //node in database where this service provider is stored


    private LinearLayout incompleteProfileLyt;


    private Button completeProfileBtn;

    private EditText companyEt;
    private EditText descEt;
    private EditText phoneEt;


    private TextView companyErrorTv;
    private TextView descErrorTv;
    private TextView phoneErrorTv;


    //constructor that allows passing arguments from main activity
    public static SpProfileFragment newInstance(String username, String id ) {
        SpProfileFragment myFrag = new SpProfileFragment();

        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("ID", id);
        myFrag.setArguments(args);

        return myFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString("username");
            id = getArguments().getString("ID");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        spNode = FirebaseDatabase.getInstance().getReference("users").child( id );

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sp_profile, container, false);

        // initializing layout components
        incompleteProfileLyt = (LinearLayout) v.findViewById(R.id.incompleteProfileLyt);

        companyEt = (EditText) v.findViewById(R.id.companyEt);
        descEt = (EditText) v.findViewById(R.id.descEt);
        phoneEt = (EditText) v.findViewById(R.id.phoneEt);


        companyErrorTv = (TextView) v.findViewById(R.id.companyError);
        descErrorTv = (TextView) v.findViewById(R.id.descError);
        phoneErrorTv = (TextView) v.findViewById(R.id.phoneError);

        phoneErrorTv.setText("Mandatory Field");
        phoneErrorTv.setVisibility(View.VISIBLE);
        companyErrorTv.setText("Mandatory Field");
        companyErrorTv.setVisibility(View.VISIBLE);




        phoneEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //set colour topink :)
                //then
                String phone =phoneEt.getText().toString().trim();
                if(phone.equals("")){
                    phoneErrorTv.setText("Mandatory Field");
                    phoneErrorTv.setVisibility(View.VISIBLE);
                }else if(!android.util.Patterns.PHONE.matcher(phone).matches()){
                    phoneErrorTv.setText("Enter a valid phone number");
                    phoneErrorTv.setVisibility(View.VISIBLE);
                }else{
                    phoneErrorTv.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                //set colour topink :)
                //then
            }
        });

        companyEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(companyEt.getText().toString().trim().equals("")){
                    companyErrorTv.setText("Mandatory Field");
                    companyErrorTv.setVisibility(View.VISIBLE);
                }else if(companyEt.getText().toString().trim().length()<3){
                    companyErrorTv.setText("Enter valid Company Name");
                    companyErrorTv.setVisibility(View.VISIBLE);
                }
                else{
                    companyErrorTv.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        completeProfileBtn = (Button) v.findViewById(R.id.completeProfileBtn);

        //closes the profile complete lyt
        completeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*boolean validInfo = validateInputs();
                if(validInfo){
                    setProfile(phoneNum, companyName, address, description);
                    //get rid of this layout, visible the other one.
                }*/
            }
        });


        return v;
    }

    /*public boolean validatePhone(){

        try {
            String phoneNumber = phoneLyt.getEditText().getText().toString().trim();

            if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
                phoneLyt.setError("Enter a valid phone number");
                return false;
            } else {
                phoneLyt.setErrorEnabled(false);
                return true;
            }
        }catch(Exception e){
            phoneLyt.setError("Enter a valid phone number");
            return false;
        }
    }*/

    /*public boolean validateAddress(){

        try {
            String address = addressLyt.getEditText().getText().toString().trim();

            if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
                phoneLyt.setError("Enter a valid phone number");
                return false;
            } else {
                phoneLyt.setErrorEnabled(false);
                return true;
            }
        }catch(Exception e){
            phoneLyt.setError("Enter a valid phone number");
            return false;
        }
    }*/

    public void addAvl(){
    }
}
