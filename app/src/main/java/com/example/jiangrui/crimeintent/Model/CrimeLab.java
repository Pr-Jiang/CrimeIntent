package com.example.jiangrui.crimeintent.Model;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jiang Rui on 2016/5/23.
 */
public class CrimeLab {
    private Context mAppContext;
    private ArrayList<Crime> mCrimes;
    private static CrimeLab sCrimeLab;         //存储单例的唯一实例
    private CriminalIntentJSONSerializer mSerializer;
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private CrimeLab(Context appContext) {     //私有构造方法，控制本单例模式仅允许创造一个实例
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes:", e);
        }

        /*测试
        for(int i = 0;i<100;i++){              //生成100个Crime
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setIsSolved(i%2==0);
            mCrimes.add(crime);
        }
        */
    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimeLab;
    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void deleteCrime(Crime crime){
        mCrimes.remove(crime);
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public boolean saveCrimes() {
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving crimes:" + e);
            return false;
        }
    }
}
