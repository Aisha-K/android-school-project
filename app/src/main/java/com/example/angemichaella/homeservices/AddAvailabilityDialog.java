package com.example.angemichaella.homeservices;

    import android.app.Dialog;
    import android.content.Context;
    import android.os.Bundle;
    import android.support.v7.app.AlertDialog;
    import android.support.v7.app.AppCompatDialogFragment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.widget.Button;
    import android.widget.NumberPicker;
    import android.widget.TextView;
    import android.widget.Toast;

public class AddAvailabilityDialog extends AppCompatDialogFragment {

        Button sat;

        NumberPicker hourPicker;
        NumberPicker minPicker;
        NumberPicker tense;

        NumberPicker toHourPicker;
        NumberPicker toMinPicker;
        NumberPicker toTense;

        TextView errorMsg;

        Button okbtn;
        Button cancelbtn;

        private AddAvailabilityListener listener;



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            /*Bundle mArgs = getArguments();

            final String title = mArgs.getString("dialog_title");
            final String name = mArgs.getString("srv_name");
            String rate = mArgs.getString("srv_rate");
            final String id = mArgs.getString("srv_id");
            String type = mArgs.getString("srv_type");*/

            AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
            LayoutInflater inf = getActivity().getLayoutInflater();
            View v = inf.inflate(R.layout.availability_dialog, null);

            b.setView(v);
            okbtn = (Button) v.findViewById(R.id.btnOk);
            cancelbtn = (Button) v.findViewById(R.id.btnCancel);


            //initializing the initial view of the dialog
            sat = (Button) v.findViewById(R.id.sat);

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
            okbtn.setOnClickListener(new View.OnClickListener() {
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

                    if(from.time()>to.time()){
                        errorMsg.setVisibility(View.VISIBLE);
                    }else{
                        listener.receiveAvailability(from, to);
                        test.dismiss();
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
            void receiveAvailability(Time from, Time to);
        }
    }
