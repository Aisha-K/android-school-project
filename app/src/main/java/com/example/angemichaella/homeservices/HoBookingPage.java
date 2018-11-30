package com.example.angemichaella.homeservices;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class HoBookingPage extends AppCompatActivity {

    ServiceProvider sp;
    String hoName;
    String spName;
    String srvName;
    String spId;

    DatabaseReference spNode;
    DatabaseReference bookingsDb;


    DatePickerDialog datePicker;
    ArrayList<String> times = new ArrayList<>();
    ArrayList<String> days = new ArrayList<>();
    Day selectedDay;

    Spinner daySpinner;
    Spinner timeSpinner;


    Button confirmBtn;

    TextView title;
    TextView spNameTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_booking_page);

        /*

        recieve these in a bundle:
        - Service providers Id
        -Service Providers name
        - Services Name
        - homeOwner ID
         */
        hoName = getIntent().getStringExtra("USER_NAME");//"home";//NEEEDS TO BE GOTTEN FROM OTER ACTIVITY
        spId = getIntent().getStringExtra("SP_ID"); //NEEEDS TO BE GOTTEN FROM OTER ACTIVITY
        spName = getIntent().getStringExtra("SP_NAME"); //Needs TO BE BROUGHT
        srvName = getIntent().getStringExtra("SRV_NAME"); //NEEDS TO BE BROUGHT FROM OTHER ACTIVITY

        spNode = FirebaseDatabase.getInstance().getReference("users").child(spId);// un service pw provider
        //hoNode = FirebaseDatabase.getInstance().getReference("users").child(hoId);
        bookingsDb = FirebaseDatabase.getInstance().getReference("bookings");

        setServiceProvider();
        selectedDay = null;

        spNameTv =findViewById(R.id.spNameTV);
        title = findViewById(R.id.bookingTitleTv);

        title.setText(srvName);
        spNameTv.setText("provided by "+ spName);

        daySpinner = findViewById(R.id.dateSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        confirmBtn = findViewById(R.id.confirmBookingBtn);
    }


    public void setTimesList(Day day){
        times.clear();
        for(Availability a: sp.availabilities){
            if(a.getDay() == day){
                times.add(a.getFrom().toString()+" to "+a.getTo().toString());
            }
        }
    }

    public void setDaySpinner(){
        ArrayAdapter<String> dayAdptr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, days);
        daySpinner.setAdapter(dayAdptr);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {
                String dayString = (String) parent.getItemAtPosition(pos);// your code here
                setTimesList(stringToDay(dayString.substring(0,3)));
                setTimesSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    public void setTimesSpinner(){
        ArrayAdapter<String> timeAdptr = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, times);
        timeSpinner.setAdapter(timeAdptr);
    }

    public void setDaysList(){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.ENGLISH);
        Date today = new Date();
        Date[] nextWeek = new Date[14];

        for(int i =0; i<14; i++){
            nextWeek[i]= new Date(today.getTime() + (i+1)*(1000 * 60 * 60 * 24)); //getting all dates for 2 weeks in advance
        }

        sdf.applyPattern("EEE, d MMM yyyy");

        ArrayList<String> dates = new ArrayList<>();
        for(int i = 0; i<14; i++){
            dates.add(sdf.format(nextWeek[i]));
        }

        for(String date: dates){
            if(sp.availableOnWeekday(stringToDay(date.substring(0,3)))){
                days.add(date);
            }
        }

        /*for(Availability a: sp.availabilities){
            if(days.indexOf(a.getDay()) < 0){
                days.add(a.getDay().toString());
            }
        }*/
    }

    private void setServiceProvider(){

        spNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                sp = dataSnapshot.getValue(ServiceProvider.class);
                setDaysList();
                setDaySpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    private Day stringToDay(String day){
        if (day.equals("Mon")){
            return Day.MONDAY;
        }
        else if (day.equals("Tue")){
            return Day.TUESDAY;
        }
        else if (day.equals("Wed")){
            return Day.WEDNESDAY;
        }
        else if (day.equals("Thu")){
            return Day.THURSDAY;
        }
        else if (day.equals("Fri")){
            return Day.FRIDAY;
        }
        else if (day.equals("Sat")){
            return Day.SATURDAY;
        }
        else if (day.equals("Sun")){
            return Day.SUNDAY;
        }
        return null;
    }

    public void requestBooking(View view){

        final String chosenDate = (String)daySpinner.getSelectedItem();
        String chosenTime = (String)timeSpinner.getSelectedItem();

        int ixOfTo = chosenTime.indexOf('t');

        String fromStr = chosenTime.substring(0, chosenTime.indexOf('M')+1);
        String toStr = chosenTime.substring(ixOfTo +3 , chosenTime.indexOf('M', ixOfTo)+1);


        int fromH = Integer.parseInt(fromStr.substring(0, fromStr.indexOf(':')));
        int fromMin = Integer.parseInt(fromStr.substring(fromStr.indexOf(':')+1, fromStr.indexOf(':')+3));
        int fromTense;
        if(fromStr.charAt(fromStr.length()-2) == 'A'){
            fromTense = 0;
        }else{
            fromTense = 1;
        }

        int toH = Integer.parseInt(toStr.substring(0, toStr.indexOf(':')));
        int toMin = Integer.parseInt(toStr.substring(toStr.indexOf(':')+1, toStr.indexOf(':')+3));
        int toTense;
        if(toStr.charAt(fromStr.length()-2) == 'A'){
            toTense = 0;
        }else{
            toTense = 1;
        }


        final Availability chosenAvl = new Availability(stringToDay(chosenDate.substring(0,3)), new Time(fromH, fromMin, fromTense), new Time(toH, toMin, toTense));


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Request Booking");
        builder.setMessage("Request "+'"' + srvName +'"'+" booking on "+ chosenDate+ " with " + spName+"?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                createBookingRequest(chosenAvl, chosenDate);

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


        String bookingInfo = "Requesting for booking on "+ chosenDate + " during the time period "+ chosenTime;//\nAVL REP: "+chosenAvl.toString();





    }

    public void createBookingRequest(Availability avl, String date){
        String bkId = bookingsDb.push().getKey();
        Booking booking = new Booking(hoName,spName, spId, srvName, avl, date, bkId);
        bookingsDb.child(bkId).setValue(booking);
        Toast.makeText(this, "Booking Request Confirmed", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, HomeOwnerNav.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
