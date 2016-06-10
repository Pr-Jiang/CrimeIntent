package com.example.jiangrui.crimeintent.Controller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.jiangrui.crimeintent.Model.Crime;
import com.example.jiangrui.crimeintent.R;
import com.example.jiangrui.crimeintent.SingleFragmentActivity;

/**
 * Created by Jiang Rui on 2016/5/24.
 */
public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks,CrimeFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detailFragmentContainer)==null){
            Intent i = new Intent(this,CrimePagerActivity.class);
            i.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());
            startActivity(i);
        }else{
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());

            if(oldDetail!=null){
                transaction.remove(oldDetail);
            }
            transaction.add(R.id.detailFragmentContainer,newDetail);
            transaction.commit();
        }
    }

    @Override
    public void onCrimeUpdate(Crime crime) {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment) fm.findFragmentById(R.id.fragmentContainer);
        listFragment.updateUI();
    }
}
