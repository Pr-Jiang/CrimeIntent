package com.example.jiangrui.crimeintent.Controller;

import android.support.v4.app.Fragment;

import com.example.jiangrui.crimeintent.SingleFragmentActivity;

/**
 * Created by Jiang Rui on 2016/5/24.
 */
public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
