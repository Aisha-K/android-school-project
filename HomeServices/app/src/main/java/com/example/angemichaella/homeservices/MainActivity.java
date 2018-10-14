package com.example.angemichaella.homeservices;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseUsers;    //reference to the database

    /*EditText editTextName;
    EditText editTextPassword;
    Button buttonCreateHomeOwner;
    Button buttonCreateServiceProvider;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing variables
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");
        //editTextName = (EditText) findViewById(R.id.txt_un);
        //editTextPassword = (EditText) findViewById(R.id.txt_pw);
    }

    public void onClickSignIn(View view) {
        EditText un = (EditText)findViewById(R.id.usernameET);
        EditText pw = (EditText)findViewById(R.id.passwordET);
        String username = un.getText().toString();
        String password = pw.getText().toString();

        if(credsMatch(username, password)){
            signIn(username);
        }else{
            displayWrongCredentialsError();
        }
    }

    public void onClickNewAcc(View view){
        EditText un = (EditText)findViewById(R.id.usernameET);
        EditText pw = (EditText)findViewById(R.id.passwordET);
        String username = un.getText().toString();
        String password = pw.getText().toString();
        int accType;

        if(isUser(username)){
            displayAlreadyUserError();
        }else{
            accType = newAccPopUp();
            if(accType!= -1){
                createAccount(username, password, accType);
                signIn(username);
            }
        }
    }

    int choice; //issa whole mess down there
    private int newAccPopUp(){
        String[] userTypes;
        int adminShift;


        if(adminExists()){
            userTypes = new String[2];
            userTypes[0] = "Home Owner";
            userTypes[1] = "Service Provider";
            adminShift = 1;
        }else{
            userTypes = new String[3];
            userTypes[0] = "Admin";
            userTypes[1] = "Home Owner";
            userTypes[2] = "Service Provider";
            adminShift = 0;

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("New Account Type: ");

        builder.setSingleChoiceItems(userTypes, -1,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
            }
        });


        builder.setPositiveButton("Create", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                System.out.println("the choice was "+ choice);
                dialog.dismiss();//user clicked create
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                choice = -1;
                dialog.dismiss();
                //user clicked cancel
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        if(choice!=-1){
            choice += adminShift;
        }

        int returnInt = choice;

        System.out.println("choice was "+ returnInt +".");
        choice = - 1;


        return returnInt;
    }


    public boolean adminExists(){}

    public void signIn(String username){}

    public void displayWrongCredentialsError(){}

    public void displayAlreadyUserError(){}

    public boolean credsMatch(String username, String password){}

    public boolean isUser(String username){}

    public void createAccount(String username, String password, int type){}

}
