package com.example.angemichaella.homeservices;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RatingDialog extends AppCompatDialogFragment
{
    //initializing variables
    RatingBar ratingBar;
    TextView ratingScale;
    EditText feedback;
    Button sendFeedback;
    double bookingRating;
    String bookingComment;

    private RatingDialog.RatingDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Bundle mArgs = getArguments();

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        LayoutInflater inf = getActivity().getLayoutInflater();
        View v = inf.inflate(R.layout.dialog_rating, null);
        b.setView(v);

        final RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        final TextView ratingScale = (TextView) v.findViewById(R.id.tvRatingScale);
        final EditText feedback = (EditText) v.findViewById(R.id.getFeedback);
        final Button sendFeedback = (Button) v.findViewById(R.id.btnSubmit);


        //implementing rating bar
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                        //rounded rating range
                        int roundedRating = (int)ratingBar.getRating();

                        ratingScale.setText(String.valueOf(v));
                        switch (roundedRating) {
                            case 1:
                                ratingScale.setText("Poor");
                                break;
                            case 2:
                                ratingScale.setText("Fair");
                                break;
                            case 3:
                                ratingScale.setText("Ok");
                                break;
                            case 4:
                                ratingScale.setText("Good");
                                break;
                            case 5:
                                ratingScale.setText("Excellent");
                                break;
                            default:
                                ratingScale.setText("");
                        }
                    }
                });

            }
        });

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedback.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity() ,"Please enter your feedback", Toast.LENGTH_LONG).show();
                } else {
                    feedback.setText("");
                    ratingBar.setRating(0);
                    Toast.makeText(getActivity(), "Thank you for your feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bookingRating = ratingBar.getRating();
        bookingComment = feedback.getText().toString();

        listener.receiveRatingUpdate(bookingRating, bookingComment);

        final AlertDialog test = b.create();
        return test;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try {
            listener = (RatingDialogListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + "must implement ESD Listener");
        }
    }

    public interface RatingDialogListener {
        void receiveRatingUpdate(double rating, String comment);
    }


}
