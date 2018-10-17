package com.example.angemichaella.homeservices;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

        //initializing database
        databaseUsers= FirebaseDatabase.getInstance().getReference("users");
    }

    public void onClickSignIn(View view) {
        EditText un = (EditText)findViewById(R.id.usernameET);
        EditText pw = (EditText)findViewById(R.id.passwordET);
        String username = un.getText().toString();
        String password = pw.getText().toString();

        trySignIn(username,password);
    }

    public void onClickNewAcc(View view){
        EditText un = (EditText)findViewById(R.id.usernameET);
        EditText pw = (EditText)findViewById(R.id.passwordET);
        String username = un.getText().toString();
        String password = pw.getText().toString();
        int accType;

        tryCreateAccount(username, password);

    }

    int choice; //issa whole mess down there
    //returns the type of account to create
    private void newAccPopUp(){
        String[] userTypes;

        if(adminExists()){
            userTypes = new String[2];
            userTypes[0] = "Home Owner";
            userTypes[1] = "Service Provider";
        }else{
            userTypes = new String[3];
            userTypes[0] = "Admin";
            userTypes[1] = "Home Owner";
            userTypes[2] = "Service Provider";
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("New Account Type: ");

        builder.setSingleChoiceItems(userTypes, -1,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choice = which;
                if(adminExists()){
                    choice++;
                }
            }
        });


        builder.setPositiveButton("Create", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){

                createAccount(choice);
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
        Toast.makeText( this , "User created acc type " + choice, Toast.LENGTH_LONG).show();
        choice = -1;
    }


    public boolean adminExists(){
        boolean exists=false;
        //
        //ADD CODE
        //
        return exists;
    }

    public void signIn(String username){
        //New intent to go to Welcome activity
        Intent intent = (new Intent(MainActivity.this, Welcome.class));
        startActivity(intent);
        //passes info to next intent which can be fetched using String data = getIntent().getExtras().getString("keyName");
        //intent.putExtra( "0", username);
    }

    //checks whether user entered the correct password, if yes, calls sign in
    //if not valid credentials, displays toast
    public User trySignIn(final String username, final String password){
        final MainActivity context=this;
        final User user[]={null};   //single User object stored in array to allow inner class method to access it

        //query gets the node where the user with parameter username exists in database
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("users").orderByChild("username").equalTo(username);
        //addlistenner allows us to retrieve the data using datasnapshot
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        //retrieving the user with parameter username
                        User retrievedUser= childSnapshot.getValue(User.class);

                        //checking if the password matches given password
                        if(retrievedUser.password.equals(password)){
                            signIn(username);
                        }
                        else{
                            Toast.makeText( context ,"Username/password not valid", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    Toast.makeText( context ,"User does not exist", Toast.LENGTH_LONG).show();
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return user[0];
    }


    public boolean tryCreateAccount(final String username, final String password){
        final MainActivity context=this;

        //query gets the node where the user with parameter username exists in database
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("users").orderByChild("username").equalTo(username);
        //addlistenner allows us to retrieve the data using datasnapshot
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //username exists
                    Toast.makeText( context , "User Already Exists", Toast.LENGTH_LONG).show();
                }
                else{ //username does not exist, create account
                    newAccPopUp();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return true;
    }


    public void createAccount(String username, String password, int type){
        //getting a unique id and we will use it as the Primary Key for our Product


        String id = databaseUsers.push().getKey();

        //creating a Product Object
        //User newUser;

        //Saving the User
        //databaseUsers.child(id).setValue(newUser);
    }


    //THIS SHIT IS FOR THE STUPID ASS POPUP
    private void createAccount(int type){
        EditText un = (EditText)findViewById(R.id.usernameET);
        EditText pw = (EditText)findViewById(R.id.passwordET);
        String username = un.getText().toString();
        String password = pw.getText().toString();
        createAccount(username, password, type);
        signIn(username);


    }

}
