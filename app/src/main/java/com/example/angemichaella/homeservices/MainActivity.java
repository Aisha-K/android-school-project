package com.example.angemichaella.homeservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;

import static com.example.angemichaella.homeservices.Sha1.hash;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseUsers;    //reference to the database

    EditText editTextName;
    EditText editTextPassword;
    /*Button buttonCreateHomeOwner;
    Button buttonCreateServiceProvider;*/

    public static final int ADMIN = 0;
    public static final int HOMEOWNER = 1;
    public static final int SERVICEPROVIDER = 2;
    private boolean adminExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //initializing database
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        updateAdmin(); //updating admin status

        //sets editTexts
        editTextName = (EditText)findViewById(R.id.usernameET);
        editTextPassword = (EditText)findViewById(R.id.passwordET);

    }


    public void onClickSignIn(View view) {

        try{
            String username = editTextName.getText().toString();
            String password = editTextPassword.getText().toString();

            // encrypting the password
            password = encrypt(password);

            username = cleanUp(username);
            trySignIn(username,password);
            username = cleanUp(username);
            trySignIn(username,password);
        }catch(Exception e){

        }

    }

    public void onClickNewAcc(View view){
        updateAdmin(); //in case admin got deleted
        try{
            String username = editTextName.getText().toString();
            tryCreateAccount(username);

        }catch(Exception e){
            //idk what to say
        }

    }

    //gets rid of spaces at the end of string
    private String cleanUp(String str){
        if(str.charAt(str.length()-1) == ' '){
            return cleanUp(str.substring(0, str.length()-1));
        }else{
            return str;
        }
    }

    // PLEASE DO NOT TOUCH THE CODE BELOW!!!!



    public void signIn(User user){


        Intent intent = null;

        if(user.getType().trim().equals("Admin")) {
            intent = (new Intent(MainActivity.this, Welcome3Admin.class));
        }
        else if (user.getType().trim().equals("HomeOwner")){
            intent = (new Intent(MainActivity.this, HomeOwnerNav.class));
        }
        else if (user.getType().trim().equals("ServiceProvider")){
            intent = (new Intent(MainActivity.this, ServiceProviderNav.class));
        }

        intent.putExtra( "USER_NAME", user.getUsername());
        intent.putExtra("USER_ID", user.getUserId());
        startActivity(intent);

    }




    //checks whether user entered the correct password, if yes, calls sign in
    //if not valid credentials, displays toast
    public void trySignIn(final String username, final String password){
        final MainActivity context=this;


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
                        if(retrievedUser.getPassword().equals(password)){
                            signIn(retrievedUser);
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
    }


    public void tryCreateAccount(final String username){//}, final String password){
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
                    goToCreateAccountPage(username);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }



    private void updateAdmin(){
        DatabaseReference rootRef=FirebaseDatabase.getInstance().getReference();
        //query gets the node where the user with parameter type = "Admin" exists in database
        Query query = rootRef.child("users").orderByChild("type").equalTo("Admin");
        //addlistener allows us to retrieve the data using datasnapshot
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    adminExists = true;
                }else{
                    adminExists = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


    // this method get the username as a string and returns true if the the string contains letter only
    // returns false otherwise
    private boolean isAlpha(String username){
        char[] letters = username.toCharArray();

        for (char letter : letters){
            if(!(Character.isLetter(letter)|| String.valueOf(letter).equals("-"))){
                return false;
            }
        }
        return true;
    }

    private void goToCreateAccountPage(String un){
        if(un == null || un.length() == 0){
            un =" ";
        }
        Intent intent = new Intent(MainActivity.this, CreateAccountPage.class);
        intent.putExtra( "USER_NAME", un);

        updateAdmin();
        intent.putExtra( "admin_exist", adminExists);
        startActivity(intent);
    }

    public static String encrypt(String p){
        String temp = "";

        try{
            temp = hash(temp);
        } catch (UnsupportedEncodingException e){

        }
        return temp;
    }
}
