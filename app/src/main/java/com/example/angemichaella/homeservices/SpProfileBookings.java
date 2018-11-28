package com.example.angemichaella.homeservices;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// this activity requires intent extra containing:
// sp id with key ID
// honame with hey USER_NAME
// service clicked on with key SRV_NAME

public class SpProfileBookings extends AppCompatActivity {

    String username;
    String id;
    String honame;

    ServiceProvider sp;// actual object
    DatabaseReference spNode; //node in database where this service provider is stored

    private TextView cCompany;
    private TextView cDesc;
    private TextView cPhone;
    private TextView cEmail;
    private TextView cName;

    private TextView cAddress;

    private Button bookButton;
    private Button goBackToRes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_profile_bookings);

        goBackToRes = findViewById(R.id.goBackToRes);
        goBackToRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToResOnClick();
            }
        });

        id = getIntent().getStringExtra("SP_ID");
        spNode = FirebaseDatabase.getInstance().getReference("users").child( id );

        setServiceProvider();
    }

    private void setServiceProvider(){

        spNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sp = dataSnapshot.getValue(ServiceProvider.class);

                /*
                private TextView cCompany;
                private TextView cDesc;
                private TextView cPhone;
                private TextView cEmail;
                private TextView cName;
                */

                setView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        }); //setting the sp....*/

    }

    public void setView(){

        bookButton =  (Button) findViewById(R.id.bookButton);
        cCompany = ( TextView ) findViewById(R.id.cptCompanyTV);
        cDesc = ( TextView ) findViewById(R.id.cptDescTV);
        cPhone = ( TextView ) findViewById(R.id.cptPhoneTV);
        cEmail = ( TextView ) findViewById(R.id.cptEmailTV);
        cName = ( TextView ) findViewById(R.id.spNameTV);
        cAddress = ( TextView ) findViewById(R.id.cptAddressTV);

        TextView licensed = (TextView) findViewById(R.id.isLicensedTV);

        cCompany.setText(sp.getCompanyName());
        cDesc.setText(sp.getDescription());
        cPhone.setText(sp.getPhoneNumber());
        cEmail.setText(sp.getEmail());
        cName.setText(sp.getUsername());
        cAddress.setText(sp.getAddress());

        if(sp.isLicensed()){
            licensed.setVisibility(View.VISIBLE);
        }else{
            licensed.setVisibility(View.GONE);
        }

        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookButtonOnClick();
            }
        });


    }

    public void bookButtonOnClick(){
        // direct to booking page

        Intent intent = null; // this should be replaced by the required data for the booking activity



        /*
        hoName = getIntent().getStringExtra("USER_NAME");//"home";//NEEEDS TO BE GOTTEN FROM OTER ACTIVITY
        spId = getIntent().getStringExtra("SP_ID"); //NEEEDS TO BE GOTTEN FROM OTER ACTIVITY
        spName = getIntent().getStringExtra("SP_NAME"); //Needs TO BE BROUGHT
        srvName = getIntent().getStringExtra("SRV_NAME"); //NEEDS TO BE BROUGHT FROM OTHER ACTIVITY
         */

        intent = (new Intent(this, HoBookingPage.class));
        intent.putExtra( "USER_NAME", getIntent().getStringExtra("USER_NAME"));
        intent.putExtra("SP_ID", id);
        intent.putExtra("SP_NAME", sp.getUsername());
        intent.putExtra("SRV_NAME", getIntent().getStringExtra("SRV_NAME"));

        startActivity(intent);
    }

    public void goBackToResOnClick(){
        // go back to sender of intent
        this.finish(); // will work if this has been called with startActivity
    }

}
