package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Stack;

public class SpManageBookingsFrag extends Fragment {

    DatabaseReference bookingsDb;
    ArrayList<Booking> myBookings;

    BookingAdapter adptr; //adapter for booking list view
    ListView bookingsListView; //list view of the users bookings
    LinearLayout emptyLyt; //layout when nothing is there
    TextView titleTv;

    TextView emptyTitle;
    TextView emptyMsg;

    String username;
    String id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString("username");
            id = getArguments().getString("ID");
            bookingsDb = FirebaseDatabase.getInstance().getReference("bookings");
            myBookings = new ArrayList<>();
            setUpMyBookingList(); //sets up myBookings with all of the users booking in the database
        }


    }
    //constructor that allows passing arguments from main activity
    public static SpManageBookingsFrag newInstance(String username, String id ) {
        SpManageBookingsFrag myFrag = new SpManageBookingsFrag();

        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString("ID", id);
        myFrag.setArguments(args);

        return myFrag;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_manage_bookings, container, false);
        bookingsListView = (ListView) view.findViewById(R.id.bookingLv);
        emptyLyt = (LinearLayout) view.findViewById(R.id.emptyBL);
        titleTv = (TextView) view.findViewById(R.id.bookfragtitle);

        emptyTitle = (TextView) view.findViewById(R.id.emptyListTitle);
        emptyMsg = (TextView) view.findViewById(R.id.emptyListMessage);

        emptyMsg.setText("We Anticipate Your First Booking!");
        return view;

    }

    public void setUpMyBookingList() {

        Query query = bookingsDb.orderByChild("serviceProviderName").equalTo(username);//queries all bookings in the booking database equal to this homeOwnersName
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myBookings.clear();

                //USING A STACK TO REVERSE THE BOOKING ORDER FORM MOST RECENT TO OLDEST
                // INSTEAD OF OLDEST TO NEWEST
                Stack<Booking> stack = new Stack<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    stack.push(postSnapshot.getValue(Booking.class));//adding all the booking to our list.
                }

                while(!stack.empty()){
                    myBookings.add(stack.pop());
                }

                if(myBookings.isEmpty()){
                    emptyLyt.setVisibility(View.VISIBLE);
                    bookingsListView.setVisibility(View.GONE);
                    titleTv.setVisibility(View.GONE);
                }else {
                    titleTv.setVisibility(View.VISIBLE);
                    emptyLyt.setVisibility(View.GONE);
                    bookingsListView.setVisibility(View.VISIBLE);
                    ///Now that the list is built, we can set up the appearance of the list view

                    if (getActivity() != null) {
                        adptr = new BookingAdapter(getActivity(), myBookings, BookingAdapter.SP); //can setup the adapter now that the list is built
                        bookingsListView.setAdapter(adptr);
                        bookingsListView.setOnItemClickListener(//here sets the onclick for the booking list view
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                        //No onclick for sp bookings
                                    }
                                }
                        );
                    }
                }

            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void update(String bookingId, final double rating, final String comment){

        final DatabaseReference booking = FirebaseDatabase.getInstance().getReference("bookings").child(bookingId);
        booking.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                Booking b = dataSnapshot.getValue(Booking.class);
                b.setRating(rating);
                b.setComment(comment);
                booking.setValue(b);

                final DatabaseReference spNode = FirebaseDatabase.getInstance().getReference("users").child(b.getServiceProviderId());
                spNode.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot){

                        ServiceProvider sp = dataSnapshot.getValue(ServiceProvider.class);
                        sp.addRating(rating);
                        spNode.setValue(sp);

                    }
                    public void onCancelled(DatabaseError databaseError){
                    }
                });

            }
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }




}
