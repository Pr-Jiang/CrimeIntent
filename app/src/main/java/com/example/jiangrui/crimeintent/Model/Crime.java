package com.example.jiangrui.crimeintent.Model;

import org.json.JSONException;
import org.json.JSONObject;

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
    private Photo mPhoto;
    private String mSuspect;
    private String mPhone;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_SUSPECT = "suspect";
    private static final String JSON_PHONE = "phone";

    public Crime() {
        mId = UUID.randomUUID();                      //获取唯一标识符
        mDate = new Date();
    }

    public Crime(JSONObject jsonObject) throws JSONException {
        mId = UUID.fromString(jsonObject.getString(JSON_ID));
        if (jsonObject.has(JSON_TITLE))
            mTitle = jsonObject.getString(JSON_TITLE);
        if (jsonObject.has(JSON_SUSPECT))
            mSuspect = jsonObject.getString(JSON_SUSPECT);
        if(jsonObject.has(JSON_PHONE))
            mPhone = jsonObject.getString(JSON_PHONE);
        mIsSolved = jsonObject.getBoolean(JSON_SOLVED);
        mDate = new Date(jsonObject.getString(JSON_DATE));
        if (jsonObject.has(JSON_PHOTO))
            mPhoto = new Photo(jsonObject.getJSONObject(JSON_PHOTO));
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

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo photo) {
        mPhoto = photo;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_SOLVED, mIsSolved);
        json.put(JSON_DATE, mDate);
        json.put(JSON_SUSPECT, mSuspect);
        json.put(JSON_PHONE,mPhone);
        if (mPhoto != null)
            json.put(JSON_PHOTO, mPhoto.toJSON());
        return json;
    }
}
