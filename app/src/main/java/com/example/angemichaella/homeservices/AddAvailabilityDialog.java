package com.example.angemichaella.homeservices;

    import android.app.Dialog;
    import android.content.Context;
    import android.os.Bundle;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatDialogFragment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.NumberPicker;
    import android.widget.TextView;
    import android.widget.Toast;

    import java.util.ArrayList;
    import java.util.List;

public class AddAvailabilityDialog extends AppCompatDialogFragment {

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

        Button addbtn;
        Button cancelbtn;

        private AddAvailabilityListener listener;



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Bundle mArgs = getArguments();


            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            LayoutInflater inf = getActivity().getLayoutInflater();
            View v = inf.inflate(R.layout.availability_dialog, null);

            b.setView(v);
            addbtn = (Button) v.findViewById(R.id.btnAdd);
            cancelbtn = (Button) v.findViewById(R.id.btnCancel);


            //initializing the initial view of the dialog
            sun = (CheckBox) v.findViewById(R.id.sun);
            mon =  (CheckBox) v.findViewById(R.id.mon);
            tues =  (CheckBox) v.findViewById(R.id.tue);
            wed =  (CheckBox) v.findViewById(R.id.wed);
            thur =  (CheckBox) v.findViewById(R.id.thur);
            fri =  (CheckBox) v.findViewById(R.id.fri);
            sat =  (CheckBox) v.findViewById(R.id.sat);


            hourPicker = (NumberPicker) v.findViewById(R.id.hourPicker);
            minPicker = (NumberPicker) v.findViewById(R.id.minutePicker);
            tense = (NumberPicker) v.findViewById(R.id.tense);

            toHourPicker = (NumberPicker) v.findViewById(R.id.toHourPicker);
            toMinPicker = (NumberPicker) v.findViewById(R.id.toMinutePicker);
            toTense = (NumberPicker) v.findViewById(R.id.toTense);

            errorMsg = (TextView) v.findViewById(R.id.errormsg);




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


            final AlertDialog test = b.create();


            final String day = mArgs.getString("day");
            final int hourF = mArgs.getInt("hourF");
            final int minF = mArgs.getInt("minF");
            final int tenseF = mArgs.getInt("tenseF");
            final int hourT= mArgs.getInt("hourT");
            final int minT = mArgs.getInt("minT");
            final int tenseT = mArgs.getInt("tenseT");

            addbtn.setText("Update");


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

            hourPicker.setValue(hourF);
            minPicker.setValue(minF);
            tense.setValue(tenseF);

            toHourPicker.setValue(hourT);
            toMinPicker.setValue(minT);
            toTense.setValue(tenseT);


            addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int fromH = hourPicker.getValue();
                    int fromM = minPicker.getValue();
                    int fromT = tense.getValue();

                    int toH = toHourPicker.getValue();
                    int toM = toMinPicker.getValue();
                    int toT = toTense.getValue();

                    Time from = new Time(fromH, fromM, fromT);
                    Time to = new Time(toH, toM, toT);

                    if(from.time()>=to.time()){
                        errorMsg.setText("TO time should be greater than FROM time");
                        errorMsg.setVisibility(View.VISIBLE);
                    }else{
                        int n = 0; //number of days chosen, used to make sure smth was selected
                        ArrayList<Day> days = new ArrayList<>();
                        if(sun.isChecked()){
                            days.add(Day.SUNDAY);
                            n++;
                        }
                        if(mon.isChecked()){
                            days.add(Day.MONDAY);
                            n++;
                        }
                        if(tues.isChecked()){
                            days.add(Day.TUESDAY);
                            n++;
                        }
                        if(wed.isChecked()){
                            days.add(Day.WEDNESDAY);
                            n++;
                        }
                        if(thur.isChecked()){
                            days.add(Day.THURSDAY);
                            n++;
                        }
                        if(fri.isChecked()){
                            days.add(Day.FRIDAY);
                            n++;
                        }
                        if(sat.isChecked()){
                            days.add(Day.SATURDAY);
                            n++;
                        }

                        if(n == 0){
                            errorMsg.setText("Choose a day, please.");
                            errorMsg.setVisibility(View.VISIBLE); //not the right eror message but just for now
                        }else{
                            listener.receiveAvailability(days, from, to);
                            test.dismiss();
                        }

                    }


                }
            });

            cancelbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    test.dismiss();
                }
            });

            return test;
        }

        @Override
        public void onAttach(Context context)
        {
            super.onAttach(context);

            try {
                listener = (AddAvailabilityListener) context;
            } catch (ClassCastException e)
            {
                throw new ClassCastException(context.toString() + "must implement AAD Listener");
            }
        }

        public interface AddAvailabilityListener {
            void receiveAvailability(List<Day> days, Time from, Time to);
        }
    }
