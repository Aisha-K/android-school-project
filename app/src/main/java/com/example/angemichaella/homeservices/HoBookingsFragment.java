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


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HoBookingsFragment extends Fragment {

    DatabaseReference bookingsDb;
    ArrayList<Booking> myBookings;
    String hoName;

    BookingAdapter adptr;
    ListView bookingsListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookingsDb = FirebaseDatabase.getInstance().getReference("bookings");
        hoName = "home"; //need to get form bundle
        myBookings = new ArrayList<>();
        setUpMyBookingList();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_manage_bookings, container, false);
        bookingsListView = (ListView) view.findViewById(R.id.bookingLv);
        return view;
    }

    public void setUpMyBookingList() {

        Query query = bookingsDb.orderByChild("homeOwnerName");//.equalTo(hoName); //orders list alphabetically based on the service name
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    myBookings.add(postSnapshot.getValue(Booking.class));//adding all the booking to our list.
                }

                adptr = new BookingAdapter(getActivity(), myBookings); //can setup the adapter now that the list is built
                bookingsListView.setAdapter(adptr);
                bookingsListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                Booking clickedBooking = (Booking) parent.getItemAtPosition(pos);
                                Toast.makeText(getActivity(), clickedBooking.toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                );

            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


}
