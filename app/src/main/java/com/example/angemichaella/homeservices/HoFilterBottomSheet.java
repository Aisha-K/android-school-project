package com.example.angemichaella.homeservices;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.design.widget.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class HoFilterBottomSheet extends BottomSheetDialogFragment{
    //current state variables

    ArrayList<Availability> chosenAvls;
    double rateLowerBound;

    FilterSpListener listener;
    CheckBox avlCb;
    CheckBox ratingCb;

    //availability view stuff
    LinearLayout avlFilterLyt;
    CheckBox sun;
    CheckBox mon;
    CheckBox tues;
    CheckBox wed;
    CheckBox thur;
    CheckBox fri;
    CheckBox sat;

    NumberPicker hourPicker;
    NumberPicker minPicker;
    NumberPicker tense;

    NumberPicker toHourPicker;
    NumberPicker toMinPicker;
    NumberPicker toTense;

    TextView errorMsg;
    TextView ratingTV;

    LinearLayout ratingFilterLyt;
    RatingBar ratingBar;
    Button doneBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chosenAvls = new ArrayList<>();
        rateLowerBound = -1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.filter_sps_bottomsheet, container, false);

        //setting day check boxes
        sun = (CheckBox) v.findViewById(R.id.sun);
        mon =  (CheckBox) v.findViewById(R.id.mon);
        tues =  (CheckBox) v.findViewById(R.id.tue);
        wed =  (CheckBox) v.findViewById(R.id.wed);
        thur =  (CheckBox) v.findViewById(R.id.thur);
        fri =  (CheckBox) v.findViewById(R.id.fri);
        sat =  (CheckBox) v.findViewById(R.id.sat);
        //setting time pickers
        hourPicker = (NumberPicker) v.findViewById(R.id.hourPicker);
        minPicker = (NumberPicker) v.findViewById(R.id.minutePicker);
        tense = (NumberPicker) v.findViewById(R.id.tense);



        toHourPicker = (NumberPicker) v.findViewById(R.id.toHourPicker);
        toMinPicker = (NumberPicker) v.findViewById(R.id.toMinutePicker);
        toTense = (NumberPicker) v.findViewById(R.id.toTense);

        hourPicker.setMinValue(1);
        hourPicker.setMaxValue(12);
        hourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%01d", i);
            }
        });

        minPicker.setMinValue(0);
        minPicker.setMaxValue(59);

        //so they see 2 digits for minutes
        minPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        toHourPicker.setMinValue(1);
        toHourPicker.setMaxValue(12);
        toHourPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%01d", i);
            }
        });

        toMinPicker.setMinValue(0);
        toMinPicker.setMaxValue(59);
        toMinPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });


        tense.setMinValue(0);
        tense.setMaxValue(1);
        tense.setDisplayedValues( new String[] { "AM", "PM"} );

        toTense.setMinValue(0);
        toTense.setMaxValue(1);
        toTense.setDisplayedValues( new String[] { "AM", "PM"} );
        //setting layouts
        avlFilterLyt = (LinearLayout)v.findViewById(R.id.chooseAvlLyt);
        ratingFilterLyt = (LinearLayout)v.findViewById(R.id.chooseRatingLyt);
        doneBtn= (Button) v.findViewById(R.id.doneFilterBtn);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        ratingTV = (TextView) v.findViewById(R.id.ratingTV);

        avlCb = (CheckBox) v.findViewById(R.id.avlCheck);
        ratingCb = (CheckBox) v.findViewById(R.id.ratingChk);

        avlCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (avlCb.isChecked()){
                    dropAvlLayout(chosenAvls);
                }else{
                    avlFilterLyt.setVisibility(View.GONE);
                }
                /*
                if is checked:
                drop down the rating ccf

                 */
            }
        });

        ratingCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ratingCb.isChecked()){
                    dropRatingLayout(rateLowerBound);
                }else{
                    ratingFilterLyt.setVisibility(View.GONE);
                }
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                rateLowerBound = rating;
                if(rating== 5){

                    ratingTV.setText(String.valueOf(rating) + " stars");
                }else{
                    ratingTV.setText(String.valueOf(rating) + " stars and above");
                }

            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Availability> avls = null;
                double low = -1;

                if(avlCb.isChecked()){
                    ArrayList<Day> days = new ArrayList<>();
                    if(sun.isChecked()){
                        days.add(Day.SUNDAY);
                    }
                    if(mon.isChecked()){
                        days.add(Day.MONDAY);
                    }
                    if(tues.isChecked()){
                        days.add(Day.TUESDAY);
                    }
                    if(wed.isChecked()){
                        days.add(Day.WEDNESDAY);
                    }
                    if(thur.isChecked()){
                        days.add(Day.THURSDAY);
                    }
                    if(fri.isChecked()){
                        days.add(Day.FRIDAY);
                    }
                    if(sat.isChecked()){
                        days.add(Day.SATURDAY);
                    }

                    try{
                        int fromH = hourPicker.getValue();
                        int fromM = minPicker.getValue();
                        int fromT = tense.getValue();

                        int toH = toHourPicker.getValue();
                        int toM = toMinPicker.getValue();
                        int toT = toTense.getValue();

                        Time from = new Time(fromH, fromM, fromT);
                        Time to = new Time(toH, toM, toT);

                        avls = new ArrayList<>();
                        for(Day d: days){
                            avls.add(new Availability(d, from, to ));
                        }
                    }catch(Exception e){
                        //display error
                    }

                }

                if(ratingCb.isChecked()){
                    low = rateLowerBound;
                }

                listener.choseFilters(low, avls);
                dismiss();
            }
        });
        avlFilterLyt.setVisibility(View.GONE);
        ratingFilterLyt.setVisibility(View.GONE);




        return v;
    }

    private void dropAvlLayout(List<Availability> avls){

        ratingFilterLyt.setVisibility(View.GONE);
        avlFilterLyt.setVisibility(View.VISIBLE);

        if(avls!= null && !avls.isEmpty()){

            for(Availability a : avls){
                Day day = a.getDay();
                if (day.equals("MONDAY")){
                    mon.setChecked(true);
                }
                else if (day.equals("TUESDAY")){
                    tues.setChecked(true);
                }
                else if (day.equals("WEDNESDAY")){
                    wed.setChecked(true);
                }
                else if (day.equals("THURSDAY")){
                    thur.setChecked(true);
                }
                else if (day.equals("FRIDAY")){
                    fri.setChecked(true);
                }
                else if (day.equals("SATURDAY")){
                    sat.setChecked(true);
                }
                else if (day.equals("SUNDAY")){
                    sun.setChecked(true);
                }
            }

            Availability modelAvl = avls.get(0);
            hourPicker.setValue(modelAvl.getFrom().getHour());
            minPicker.setValue(modelAvl.getFrom().getMinute());
            tense.setValue(modelAvl.getFrom().getTense());

            toHourPicker.setValue(modelAvl.getTo().getHour());
            toMinPicker.setValue(modelAvl.getTo().getMinute());
            toTense.setValue(modelAvl.getTo().getTense());
        }
    }

    private void dropRatingLayout(double low){
        ratingFilterLyt.setVisibility(View.VISIBLE);
        avlFilterLyt.setVisibility(View.GONE);
    }




    public void onAttach(Context c){
        super.onAttach(c);

        try{
            listener = (FilterSpListener) c;
        }catch (ClassCastException e){
            throw new ClassCastException(c.toString()+ " must implement FilterSpListener!");
        }

    }
    public interface FilterSpListener{
        public void choseFilters(double ratingLowerBound,  List<Availability> avls);
    }


}
