package com.example.jiangrui.crimeintent.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.jiangrui.crimeintent.Model.Crime;
import com.example.jiangrui.crimeintent.Model.CrimeLab;
import com.example.jiangrui.crimeintent.R;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jiang Rui on 2016/5/26.
 */
public class CrimePagerActivity extends FragmentActivity implements CrimeFragment.Callbacks{
    private ViewPager mViewPager;
    private ArrayList<Crime> mCrimes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建布局
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        //实现点击哪个列表项跳转到相对应的详情页面功能
        UUID crimeId = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        Crime crime = CrimeLab.get(this).getCrime(crimeId);
        for (int i = 0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                this.setTitle(crime.getTitle());
                break;
            }
        }
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Crime crime = mCrimes.get(position);
                if(crime.getTitle()!=null)
                    setTitle(crime.getTitle());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onCrimeUpdate(Crime crime) {

    }
}
