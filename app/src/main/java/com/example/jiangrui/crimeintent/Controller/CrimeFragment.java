package com.example.jiangrui.crimeintent.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.jiangrui.crimeintent.Model.Crime;
import com.example.jiangrui.crimeintent.Model.CrimeLab;
import com.example.jiangrui.crimeintent.R;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jiang Rui on 2016/5/16.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    public static final String EXTRA_CRIME_ID = "com.example.jiangrui.crimeintent.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final String DIALOG_SELECT = "select";
    private static final int REQUEST_SELECT = 2;
    public static final int REQUEST_DATE = 0;
    public static final int REQUEST_TIME = 1;

    public static CrimeFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mCrime = new Crime();
//        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);   //fragment argument 方法获取EXTRA_CRIME_ID
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                SelectDialogFragment selectDialog = new SelectDialogFragment();
                selectDialog.setTargetFragment(CrimeFragment.this, REQUEST_SELECT);
                selectDialog.show(fragmentManager, DIALOG_SELECT);
            }
        });
//        mDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////                DatePickerFragment dialog = new DatePickerFragment();
//                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
//                /*
//                设置目标Fragment
//                当 dialog被取消后，dialog调用目标fragment(CrimeFragment)的onActivityResult()方法
//                 */
//                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
//                //show(FragmentManager,String)中 String参数唯一识别存放在FragmentManager队列中的DialogFragment
//                dialog.show(fragmentManager, DIALOG_DATE);
//            }
//        });
//        mTimeButton = (Button) view.findViewById(R.id.crime_time);
//        mTimeButton.setText(mCrime.getDate().toString());
//        mTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
//                timeDialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
//                timeDialog.show(fragmentManager,DIALOG_TIME);
//            }
//        });
        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setIsSolved(isChecked);          //Set the crime's solved property
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_SELECT) {
            int mChoice = data.getIntExtra(SelectDialogFragment.EXTRA_CHOICE, 3);
            if (mChoice == 3)
                return;
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (mChoice == SelectDialogFragment.CHOICE_DATE) {
                DatePickerFragment dateDialog = DatePickerFragment.newInstance(mCrime.getDate());
                dateDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dateDialog.show(fragmentManager, DIALOG_DATE);

            }
            else if (mChoice == SelectDialogFragment.CHOICE_TIME) {
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
                timeDialog.setTargetFragment(CrimeFragment.this,REQUEST_TIME);
                timeDialog.show(fragmentManager,DIALOG_TIME);
            }
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
        if(requestCode == REQUEST_TIME){
                Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
                mCrime.setDate(date);
                updateDate();
        }

    }

    public void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }
}
