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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SpProfileFragment extends Fragment {
    String username;
    String id;

    ServiceProvider sp;// actual object
    DatabaseReference spNode; //node in database where this service provider is stored

    private LinearLayout incompleteProfileLyt;
    private LinearLayout completeProfileLyt;
    private LinearLayout loadingLyt;


    private Button completeProfileBtn;

    private EditText companyEt;
    private EditText descEt;
    private EditText phoneEt;



    private TextView companyErrorTv;
    private TextView descErrorTv;
    private TextView phoneErrorTv;
    private CheckBox licensedBtn;

    private TextView cCompany;
    private TextView cDesc;
    private TextView cPhone;
    private TextView cEmail;
    private TextView cName;




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
            spNode = FirebaseDatabase.getInstance().getReference("users").child( id );

            //BOOTLEG WORK BC IDK HOW TO DO IT ANY OTHER WA

            setServiceProvider();
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sp_profile, container, false);
        incompleteProfileLyt = (LinearLayout) v.findViewById(R.id.incompleteProfileLyt);
        completeProfileLyt = (LinearLayout) v.findViewById(R.id.profileLyt);
        loadingLyt = (LinearLayout) v.findViewById(R.id.loadingScreen);

        incompleteProfileLyt.setVisibility(View.GONE);
        completeProfileLyt.setVisibility(View.GONE);
        loadingLyt.setVisibility(View.VISIBLE);

        return v;
    }

    public boolean validatePhone(){

        try {
            String phoneNumber = phoneEt.getText().toString().trim();

            if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches()) {
                return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public boolean validateCompany(){

        try {
            String company = companyEt.getText().toString().trim();

            if (company.length() <2){
                return false;
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }



    public void updateSp(){
        spNode.removeValue();
        spNode.setValue(sp);
    }

    public void setUpIncompleteProfileView(){
        completeProfileLyt.setVisibility(View.GONE);
        incompleteProfileLyt.setVisibility(View.VISIBLE);

        View v = getView();

        companyEt = (EditText) v.findViewById(R.id.companyEt);
        descEt = (EditText) v.findViewById(R.id.descEt);
        phoneEt = (EditText) v.findViewById(R.id.phoneEt);
        licensedBtn = (CheckBox) v.findViewById(R.id.licenseCheckbox);



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
        completeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validatePhone() && validateCompany()){

                    String phoneNum = phoneEt.getText().toString().trim();
                    String companyName = companyEt.getText().toString().trim();
                    String description = null;
                    boolean isLicensed = licensedBtn.isChecked();
                    try{
                        description = descEt.getText().toString().trim();
                    }catch(Exception e){
                        //keep it null
                    }
                    sp.setProfileInfo(phoneNum, companyName, description, isLicensed);
                    updateSp();
                    //get rid of this layout, visible the other one.
                    incompleteProfileLyt.setVisibility(View.GONE);
                    completeProfileLyt.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    public void setCompleteProfileView(){


        View v = getView();

        cCompany = ( TextView ) v.findViewById(R.id.cptCompanyTV);
        cDesc = ( TextView ) v.findViewById(R.id.cptDescTV);
        cPhone = ( TextView ) v.findViewById(R.id.cptPhoneTV);
        cEmail = ( TextView ) v.findViewById(R.id.cptEmailTV);
        cName = ( TextView ) v.findViewById(R.id.spNameTV);

        TextView licensed = (TextView) v.findViewById(R.id.isLicensedTV);


        String phone = sp.getPhoneNumber();
        if(sp.getPhoneNumber().length() == 10){
            String adphn = "("+phone.substring(0,3)+")-"+phone.substring(3,6)+"-"+phone.substring(6,10);
            cPhone.setText(adphn);
        }else{
            cPhone.setText(phone);
        }

        cCompany.setText(sp.getCompanyName());
        cDesc.setText(sp.getDescription());
        cEmail.setText(sp.getEmail());
        cName.setText(sp.getUsername());
        if(sp.isLicensed()){
            licensed.setVisibility(View.VISIBLE);
        }else{
            licensed.setVisibility(View.GONE);
        }

        incompleteProfileLyt.setVisibility(View.GONE);
        completeProfileLyt.setVisibility(View.VISIBLE);
    }

    private void setServiceProvider(){

        spNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    sp = dataSnapshot.getValue(ServiceProvider.class);
                    // initializing layout components
                    loadingLyt.setVisibility(View.GONE);

                    if(sp.isProfileCompleted()){
                        setCompleteProfileView();
                    }else{
                        setUpIncompleteProfileView();
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        }); //setting the sp....*/

    }

}
