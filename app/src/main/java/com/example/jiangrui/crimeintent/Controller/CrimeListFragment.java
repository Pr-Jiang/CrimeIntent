package com.example.jiangrui.crimeintent.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jiangrui.crimeintent.Model.Crime;
import com.example.jiangrui.crimeintent.Model.CrimeLab;
import com.example.jiangrui.crimeintent.R;

import java.util.ArrayList;

/**
 * Created by Jiang Rui on 2016/5/23.
 */
public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private static final String TAG = "CrimeListFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.crimes_title);
        mCrimes = CrimeLab.get(getActivity()).getCrimes();

//        ArrayAdapter<Crime> adapter = new ArrayAdapter<Crime>(getActivity(),
//                android.R.layout.simple_list_item_1,mCrimes);
        CrimeAdapter crimeAdapter = new CrimeAdapter(mCrimes);
        setListAdapter(crimeAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime crime = ((CrimeAdapter) getListAdapter()).getItem(position);
//        Log.d(TAG,crime.getTitle()+"was clicked");

        Intent intent = new Intent(getActivity(),CrimePagerActivity.class);      //点击后从列表跳转到详情页面
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID,crime.getId());        //传递被点击的crime 的 ID
        startActivity(intent);
    }

    /*
    从CrimeActivity回退到CrimeListActivity
    操作系统会调用Activity的onResume()
    Activity的的FragmentManager调用被当前Activity托管的fragment的onResume()方法
     */
    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();      //更新列表数据
    }

    private class CrimeAdapter extends ArrayAdapter<Crime>{

        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(),0,crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){                             //If we weren't given a view, inflate one
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime,null);
            }
            Crime crime = getItem(position);                     //Configure the view for this Crime

            TextView TitleTextView = (TextView) convertView.findViewById(R.id.crime_list_item_titleTextView);
            TextView DateTextView = (TextView) convertView.findViewById(R.id.crime_list_item_dateTextView);
            CheckBox solvedCheckBox = (CheckBox) convertView.findViewById(R.id.crime_list_item_solvedCheckBox);

            TitleTextView.setText(crime.getTitle());
            DateTextView.setText(crime.getDate().toString());
            solvedCheckBox.setChecked(crime.isSolved());

            return convertView;
        }
    }
}
