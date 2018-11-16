package com.example.angemichaella.homeservices;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
            String password = editTextPassword.getText().toString();

            if(username.charAt(0) == ' '){
                Toast.makeText( this ,"Username cannot start with a space", Toast.LENGTH_LONG).show();
            }else if(cleanUp(username).length() < 3){
                Toast.makeText( this ,"Username must be at least 3 characters", Toast.LENGTH_LONG).show();
            }else if(password.length() < 3) {
                Toast.makeText(this, "Password must be at least 3 characters", Toast.LENGTH_LONG).show();
            } else if (!isAlpha(username)) {
                Toast.makeText(this , "Username should contains letters or dashes only", Toast.LENGTH_LONG).show();
            }else{
                username = cleanUp(username);
                tryCreateAccount(username, password);
            }
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


    int choice; //issa whole mess down there
    private void newAccPopUp(){
        String[] userTypes;
        if(adminExists){
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
                if(adminExists){
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

    }


    // PLEASE DO NOT TOUCH THE CODE BELOW!!!!



    public void signIn(User user){


        Intent intent = null;

        if(user.type.trim().equals("Admin")) {
            intent = (new Intent(MainActivity.this, Welcome3Admin.class));
        }
        else if (user.type.trim().equals("HomeOwner")){
            intent = (new Intent(MainActivity.this, WelcomeHomeOwner.class));
        }
        else if (user.type.trim().equals("ServiceProvider")){//GOES STRAIGHT TO THE AVAILABILITY SCREEN JUST FOR TESTING
            intent = (new Intent(MainActivity.this, ServiceProviderNav.class));//WelcomeServiceProvider.class));
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
                        if(retrievedUser.password.equals(password)){
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

    public User createAccount(String username, String password, int type){
        //getting a unique id and we will use it as the Primary Key for our Product
        // return the new user


        String id = databaseUsers.push().getKey();
        User newUser = null;
        if(type == HOMEOWNER){
            newUser = new HomeOwner(username, password, id);

        }else if (type == SERVICEPROVIDER){
            newUser = new ServiceProvider(username, password, id);
        } else if(type == ADMIN){
            newUser = new Admin(username, password, id);
        }
        //Saving the User

        databaseUsers.child(id).setValue(newUser);
        return newUser;


    }

    private void createAccount(int type){
        EditText un = (EditText)findViewById(R.id.usernameET);
        EditText pw = (EditText)findViewById(R.id.passwordET);
        String username = un.getText().toString();
        String password = pw.getText().toString();

        signIn(createAccount(username, password, type));
        updateAdmin();//in case someone just made an admin account
        choice = -1; //resetting

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
}
