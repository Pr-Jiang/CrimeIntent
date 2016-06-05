package com.example.jiangrui.crimeintent.Controller;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.example.jiangrui.crimeintent.SingleFragmentActivity;

/**
 * Created by Jiang Rui on 2016/6/4.
 */
public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Hide the window title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Hide the status bar and other OS-level chrome
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
