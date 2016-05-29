package com.example.jiangrui.crimeintent.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.example.jiangrui.crimeintent.Model.Crime;
import com.example.jiangrui.crimeintent.R;

import java.util.Date;

/**
 * Created by Jiang Rui on 2016/5/28.
 */
public class SelectDialogFragment extends DialogFragment {
    private String[] mSelect = new String[]{"Modify Date", "Modify Time"};
    private int mChoice = 3;
    public static final int CHOICE_DATE = 0;
    public static final int CHOICE_TIME = 1;
    public static final String EXTRA_CHOICE = "com.example.jiangrui.crimeintent.choice";

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CHOICE, mChoice);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    //
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.select_title)
                .setItems(mSelect, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getActivity(),"you clicked "+mSelect[which],Toast.LENGTH_SHORT).show();
                        switch (which) {
                            case 0: {
                                mChoice = CHOICE_DATE;
                                sendResult(Activity.RESULT_OK);
                                break;
                            }
                            case 1: {
                                mChoice = CHOICE_TIME;
                                sendResult(Activity.RESULT_OK);
                                break;
                            }
                        }

                    }
                })
                .create();
    }
}
