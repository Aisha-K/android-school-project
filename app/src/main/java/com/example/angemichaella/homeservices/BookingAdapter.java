package com.example.angemichaella.homeservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BookingAdapter extends ArrayAdapter<Booking> {



    public BookingAdapter(Context context, ArrayList<Booking> bookings){
        super(context, R.layout.av_row_layout, bookings); //context, list item template layout, list of items 2be displayed
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View myRowView = inflator.inflate(R.layout.booking_row, parent, false);

        Booking b = getItem(position);

        TextView provider = (TextView) myRowView.findViewById(R.id.providerTv);
        TextView srvName = (TextView) myRowView.findViewById(R.id.serviceNameTv);
        TextView rating = (TextView) myRowView.findViewById(R.id.ratingTv);
        TextView date = (TextView) myRowView.findViewById(R.id.bookingDatetv);

        provider.setText("Provided by "+ b.getServiceProviderName());
        srvName.setText(b.getServiceName());
        date.setText(b.getDate());

        /*if(b.isRated()){
            rating.setText("Rating: "+ b.getRating());
        }*/
        
        //attempt to show Rating Dialog when Booking is clicked 
        myRowView.setOnClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemCLick(AdapterView<?> parent, View view, int pos, long id)
                    {
                        Booking clickedBooking = (Booking) parent.getItemAtPosition(pos);

                        Bundle args = new Bundle();
                        args.putString("dialog_title", "Rate Booking");
                        args.putString("booking_name", clickedBooking.getServiceName());

                        RatingDialog d = new RatingDialog();
                        d.setArguments(args);
                        d.show(getActivity().getSupportFragmentManager(), "rate booking dialog");
                    }
                }
        );

        return myRowView;

    }
}
