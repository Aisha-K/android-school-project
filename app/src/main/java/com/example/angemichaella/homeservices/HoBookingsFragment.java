package com.example.angemichaella.homeservices;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HoBookingsFragment extends Fragment {

    DatabaseReference bookingsDb;
    ArrayList<Booking> myBookings;

    BookingAdapter adptr; //adapter for booking list view
    ListView bookingsListView; //list view of the users bookings


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingsDb = FirebaseDatabase.getInstance().getReference("bookings");
        myBookings = new ArrayList<>();
        setUpMyBookingList(); //sets up myBookings with all of the users booking in the database
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_manage_bookings, container, false);
        bookingsListView = (ListView) view.findViewById(R.id.bookingLv);
        return view;

    }

    public void setUpMyBookingList() {

        Query query = bookingsDb.orderByChild("homeOwnerName").equalTo(((HomeOwnerNav)getActivity()).username);//queries all bookings in the booking database equal to this homeOwnersName
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //USING A STACK TO REVERSE THE BOOKING ORDER FORM MOST RECENT TO OLDEST
                // INSTEAD OF OLDEST TO NEWEST
                Stack<Booking> stack = new Stack<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    stack.push(postSnapshot.getValue(Booking.class));//adding all the booking to our list.
                }

                while(!stack.empty()){
                    myBookings.add(stack.pop());
                }

                ///Now that the list is built, we can set up the appearance of the list view

                if(getActivity() != null){
                    adptr = new BookingAdapter(getActivity(), myBookings); //can setup the adapter now that the list is built
                    bookingsListView.setAdapter(adptr);
                    bookingsListView.setOnItemClickListener(//here sets the onclick for the booking list view
                            new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                    Booking clickedBooking = (Booking) parent.getItemAtPosition(pos);
                                    Toast.makeText(getActivity(), clickedBooking.toString(), Toast.LENGTH_LONG).show();
                                }
                            }
                    );
                }


            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
