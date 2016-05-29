package com.example.jiangrui.crimeintent.Controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jiangrui.crimeintent.R;
import com.example.jiangrui.crimeintent.SingleFragmentActivity;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
//        return new CrimeFragment();

        //fragment argument方法获取EXTRA_CRIME_ID
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(crimeId);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
//
//        if(fragment==null){
//            fragment = new CrimeFragment();
//            fragmentManager.beginTransaction()
//                    .add(R.id.fragmentContainer,fragment)
//                    .commit();
//        }
//
//    }
}
