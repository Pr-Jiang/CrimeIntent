package com.example.jiangrui.crimeintent.Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Jiang Rui on 2016/5/16.
 */
public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mIsSolved;

    public Crime() {
        mId = UUID.randomUUID();                      //获取唯一标识符
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Date getDate() {
//        DateFormat dateFormat = new SimpleDateFormat("EE,MM,dd,yyyy");
//        return dateFormat.format(mDate);
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public boolean isSolved() {
        return mIsSolved;
    }

    public void setIsSolved(boolean isSolved) {
        mIsSolved = isSolved;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
