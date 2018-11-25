package com.example.angemichaella.homeservices;


import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;

import static com.example.angemichaella.homeservices.Sha1.hash;

public class CreateAccountPage extends AppCompatActivity {

    private TextInputLayout username;
    private TextInputLayout password;
    private TextInputLayout email;
    private RadioButton adminBtn;
    private RadioGroup type;
    private DatabaseReference databaseUsers;    //reference to the database
    private boolean adminExists;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_page);

        String name = getIntent().getStringExtra("USER_NAME");
        adminExists = getIntent().getExtras().getBoolean("admin_exist");


        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        adminBtn = (RadioButton)findViewById(R.id.rbad);
        username = (TextInputLayout) findViewById(R.id.inputUn);
        password = (TextInputLayout) findViewById(R.id.inputPassword);
        email = (TextInputLayout) findViewById(R.id.inputEmail);
        type = (RadioGroup) findViewById(R.id.userType);
        if(adminExists){
            adminBtn.setVisibility(View.GONE);
        }


        if(!name.equals(" ")){
            username.getEditText().setText(name);
        }
    }

    public void onClickCreateAccount(View v){

        tryCreateAccount();
        updateAdmin();

    }

    private boolean typeChosen(){
        if(type.getCheckedRadioButtonId()==-1){
            Toast.makeText(this, "Choose an account type", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean validateEmail(){
        try {

            //for checking email dupes
            /*DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            Query query = rootRef.child("users").orderByChild("email").equalTo(emailStr);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {//username exists
                        email.setError("This email has been taken");

                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });*/

            String emailStr = email.getEditText().getText().toString().trim();

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()){
                email.setError("Please enter a valid email");
                return false;
            }else {
                email.setErrorEnabled(false);
                return true;
            }



        }catch(Exception e){
            email.setError("Please enter a valid email");
            return false;
        }

    }


    private boolean validatePassword(){

        try {
            String pwStr = password.getEditText().getText().toString();

            int n = pwStr.length();
            if (n < 3) {
                password.setError("Password must be 3 or more characters");
                return false;
            } else {
                password.setErrorEnabled(false);
                return true;
            }
        }catch(Exception e){
            password.setError("Password must be 3 or more characters");
            return false;
        }

    }


    public void tryCreateAccount(){
        final CreateAccountPage context=this;

        try {
            String unStr = username.getEditText().getText().toString().trim();
            final int n = unStr.length();


            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            Query query = rootRef.child("users").orderByChild("username").equalTo(unStr);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {//username exists
                        username.setError("This username has been taken");
                    } else if (n == 0) {
                        username.setError("Please enter a valid username");
                    } else if (n < 3 || n > 20) {
                        username.setError("Username must be between 3 and 20 characters");
                    } else { //username is good
                        username.setErrorEnabled(false);
                        boolean validEmail = validateEmail();
                        boolean validPw = validatePassword();
                        boolean typeChosen = typeChosen();
                        if(validEmail  && validPw && typeChosen){
                            signIn(createAccount());
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }catch(Exception e){
            username.setError("Please enter a valid username");
        }

    }

    private User createAccount(){
        String emailStr = email.getEditText().getText().toString().trim();
        String unStr = username.getEditText().getText().toString().trim();
        String pwStr = password.getEditText().getText().toString().trim();

        // encrypt password
        pwStr = MainActivity.encrypt(pwStr);

        User newUser = null;
        String id = databaseUsers.push().getKey();

        if (type.getCheckedRadioButtonId() == R.id.rbsp){
            newUser = new ServiceProvider(unStr, pwStr, emailStr, id);
        }else if (type.getCheckedRadioButtonId() == R.id.rbho){
            newUser = new HomeOwner(unStr, pwStr, emailStr, id);
        }else{
            newUser = new Admin(unStr, pwStr, emailStr, id);
        }

        databaseUsers.child(id).setValue(newUser);
        return newUser;
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
                    adminBtn.setVisibility(View.GONE);
                }else{
                    adminExists = false;
                    adminBtn.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void signIn(User user){


        Intent intent = null;

        if(user.getType().trim().equals("Admin")) {
            intent = (new Intent(CreateAccountPage.this, Welcome3Admin.class));
        }
        else if (user.getType().trim().equals("HomeOwner")){
            intent = (new Intent(CreateAccountPage.this, HomeOwnerNav.class));
        }
        else if (user.getType().trim().equals("ServiceProvider")){//goes to navigator
            intent = (new Intent(CreateAccountPage.this, ServiceProviderNav.class));
        }
        intent.putExtra( "USER_NAME", user.getUsername());
        intent.putExtra("USER_ID", user.getUserId());
        startActivity(intent);

    }
}
