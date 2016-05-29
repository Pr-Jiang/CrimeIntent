package com.example.jiangrui.crimeintent.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.DatePicker;

import com.example.jiangrui.crimeintent.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jiang Rui on 2016/5/26.
 */
public class DatePickerFragment extends android.support.v4.app.DialogFragment {
    private Date mDate;
    public static final String EXTRA_DATE = "com.example.jiangrui.crimeintent.date";

    public static DatePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE,date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private void sendResult(int resultCode){
        if(getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE,mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date) getArguments().getSerializable(EXTRA_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        //DatePicker对象的初始化需要整数形式的年月日，所以通过Calendar对象返回所需信息
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_date, null);
        DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //Translate year,month,day into object using a calendar
                mDate = new GregorianCalendar(year,monthOfYear,dayOfMonth).getTime();
                //Update argument to preserve selected value on rotation
                getArguments().putSerializable(EXTRA_DATE,mDate);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }
}
