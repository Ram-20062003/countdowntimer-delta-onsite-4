package com.example.countdowntimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Time_Dialog extends AppCompatDialogFragment {
int hour,min,sec;
public Time_Dialog_DataExchange time_dialog_dataExchange;
    private static final String TAG = "Time_Dialog";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_set_time,null);
        builder.setView(view).setTitle("Pick Time").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              int h=hour;
              int m=min;
              int s=sec;
              time_dialog_dataExchange.apply(h,m,s);
            }
        });
        NumberPicker numberPicker=view.findViewById(R.id.hours);
        numberPicker.setMaxValue(24);
        numberPicker.setMinValue(0);
        NumberPicker numberPicker_min=view.findViewById(R.id.minutes);
        numberPicker_min.setMaxValue(60);
        numberPicker_min.setMinValue(0);
        NumberPicker numberPicker_sec=view.findViewById(R.id.seconds);
        numberPicker_sec.setMaxValue(60);
        numberPicker_sec.setMinValue(0);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hour=numberPicker.getValue();

            }
        });
        numberPicker_min.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                min=numberPicker_min.getValue();
            }
        });
        numberPicker_sec.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                sec=numberPicker_sec.getValue();
                Log.d(TAG, "onClick: "+hour+min+sec);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        time_dialog_dataExchange= (Time_Dialog_DataExchange) context;
    }

    public interface Time_Dialog_DataExchange
    {
        void apply(int hour,int minute,int second);
    }
}
