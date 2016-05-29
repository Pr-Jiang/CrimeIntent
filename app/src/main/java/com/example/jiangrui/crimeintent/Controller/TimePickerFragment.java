package com.example.jiangrui.crimeintent.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TimePicker;

import com.example.jiangrui.crimeintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jiang Rui on 2016/5/28.
 */
public class TimePickerFragment extends DialogFragment {
    private Date mDate;
    public static final String EXTRA_TIME = "com.example.crimeintent.time";

    public static TimePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME,date);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date) getArguments().getSerializable(EXTRA_TIME);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time,null);
        TimePicker timePicker = (TimePicker) v.findViewById(R.id.dialog_date_timePicker);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR));
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mDate = new GregorianCalendar(year, month, day, hourOfDay, minute).getTime();
                getArguments().putSerializable(EXTRA_TIME, mDate);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.time_picker_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();

    }
    private void sendResult(int resultCode){
        if(getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME,mDate);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
