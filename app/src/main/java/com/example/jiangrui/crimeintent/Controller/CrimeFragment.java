package com.example.jiangrui.crimeintent.Controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.jiangrui.crimeintent.Model.Crime;
import com.example.jiangrui.crimeintent.Model.CrimeLab;
import com.example.jiangrui.crimeintent.Model.Photo;
import com.example.jiangrui.crimeintent.PictureUtils;
import com.example.jiangrui.crimeintent.R;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jiang Rui on 2016/5/16.
 */
public class CrimeFragment extends Fragment {
    private static final String TAG = "CrimeFragment";
    private Crime mCrime;
    private EditText mTitleField;
    private ImageView mPhotoView;
    private ImageButton mPhotoButton;
    private Button mDateButton;
    private Button mSuspectButton;
    private Button mReportButton;
    private Button mCallButton;
    private Button mDeleteButton;
    private CheckBox mSolvedCheckBox;
    public static final String EXTRA_CRIME_ID = "com.example.jiangrui.crimeintent.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final String DIALOG_SELECT = "select";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_SELECT = 0;
    public static final int REQUEST_DATE = 1;
    public static final int REQUEST_TIME = 2;
    private static final int REQUEST_PHOTO = 3;
    private static final int REQUEST_CONTACT = 4;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeUpdate(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static CrimeFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public UUID getCrimeId(){
        return (UUID) getArguments().getSerializable(EXTRA_CRIME_ID); //fragment argument 方法获取EXTRA_CRIME_ID
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        mCrime = new Crime();
//        UUID crimeId = (UUID) getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        UUID crimeId = getCrimeId();
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Nullable
    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mPhotoView = (ImageView) view.findViewById(R.id.crime_imageView);
        registerForContextMenu(mPhotoView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Photo photo = mCrime.getPhoto();
                if (photo == null)
                    return;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path)
                        .show(fragmentManager, DIALOG_IMAGE);
            }
        });
        mPhotoButton = (ImageButton) view.findViewById(R.id.crime_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(intent, REQUEST_PHOTO);
            }
        });
        //If camera is not available,disable camera funcationality
        PackageManager packageManager = getActivity().getPackageManager();
        boolean hasACamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                || packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD
                || Camera.getNumberOfCameras() > 0;
        if (!hasACamera)
            mPhotoButton.setEnabled(false);

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                mCallbacks.onCrimeUpdate(mCrime);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mDateButton = (Button) view.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                SelectDialogFragment selectDialog = new SelectDialogFragment();
                selectDialog.setTargetFragment(CrimeFragment.this, REQUEST_SELECT);
                selectDialog.show(fragmentManager, DIALOG_SELECT);
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setIsSolved(isChecked);          //Set the crime's solved property
                mCallbacks.onCrimeUpdate(mCrime);
            }
        });
//        boolean isReportbtnSafe = isIntentActionExist(Intent.ACTION_SEND);
        mReportButton = (Button) view.findViewById(R.id.crime_reportButton);
//        if (!isReportbtnSafe)
//            mReportButton.setEnabled(false);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });
        boolean isSuspectbtnSafe = isIntentActionExist(Intent.ACTION_PICK);
        mSuspectButton = (Button) view.findViewById(R.id.crime_suspectButton);
        if (!isSuspectbtnSafe)
            mSuspectButton.setEnabled(false);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });
        if (mCrime.getSuspect() != null)
            mSuspectButton.setText(mCrime.getSuspect());         //将mSuspectButton按钮文字设置为suspect

        mCallButton = (Button) view.findViewById(R.id.crime_callButton);
        if (mCrime.getPhone() != null)
            mCallButton.setText(mCrime.getPhone());
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri suspectPhone = Uri.parse("tel:" + mCrime.getPhone());
                Intent i = new Intent(Intent.ACTION_DIAL, suspectPhone);
                startActivity(i);
            }
        });
        mDeleteButton = (Button) view.findViewById(R.id.crime_delete_button);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeListFragment.removeFragment(mCrime,getActivity().getSupportFragmentManager());
                getActivity().setTitle(R.string.crimes_title);
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                Intent intent = new Intent(getActivity(), CrimeListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                      //设置应用图标的返回功能
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());            //如果当前Activity有父Activity，返回
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (mPhotoView.getDrawable() != null)
            getActivity().getMenuInflater().inflate(R.menu.crimefragment_photo_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_photo_delete:
                deletePhoto();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_SELECT) {
            int mChoice = data.getIntExtra(SelectDialogFragment.EXTRA_CHOICE, 3);
            if (mChoice == 3)
                return;
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            if (mChoice == SelectDialogFragment.CHOICE_DATE) {
                DatePickerFragment dateDialog = DatePickerFragment.newInstance(mCrime.getDate());
                 /*
                设置目标Fragment
                当 dialog被取消后，dialog调用目标fragment(CrimeFragment)的onActivityResult()方法
                 */
                dateDialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                //show(FragmentManager,String)中 String参数唯一识别存放在FragmentManager队列中的DialogFragment
                dateDialog.show(fragmentManager, DIALOG_DATE);

            } else if (mChoice == SelectDialogFragment.CHOICE_TIME) {
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
                timeDialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                timeDialog.show(fragmentManager, DIALOG_TIME);
            }
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mCallbacks.onCrimeUpdate(mCrime);
            updateDate();
        }
        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            mCallbacks.onCrimeUpdate(mCrime);
            updateDate();
        }
        if (requestCode == REQUEST_PHOTO) {
            String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Photo photo = new Photo(filename);
                mCrime.setPhoto(photo);
                showPhoto();
            }

        }
        if (requestCode == REQUEST_CONTACT) {

            Uri contactUri = data.getData();
            CursorLoader cursorLoader = new CursorLoader(getActivity(), contactUri, null, null, null, null);
            Cursor c = cursorLoader.loadInBackground();
            String contactId = null;
            String suspect = null;
            if (c.moveToFirst()) {       //如果查询到指定联系人
                //获取联系人的ID
                contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                //获取联系人姓名
                suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            }
            //查询指定联系人的号码
            Cursor phoneCursor = getActivity().getContentResolver()
                    .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            String phone = null;
            if (phoneCursor.moveToFirst())//如果查到指定联系人的号码
                phone = phoneCursor.getString(
                        phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));//获取号码

            phoneCursor.close();
            c.close();

            mCrime.setSuspect(suspect);
            mCrime.setPhone(phone);
            mCallbacks.onCrimeUpdate(mCrime);

            mSuspectButton.setText(suspect);
            mCallButton.setText(phone);


        }

    }

    public void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private void showPhoto() {
        Photo photo = mCrime.getPhoto();
        BitmapDrawable bitmapDrawable = null;
        if (photo != null) {
            String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(bitmapDrawable);
    }

    private void deletePhoto() {
        Photo photo = mCrime.getPhoto();
        if (photo != null) {
            String path = getActivity().getFileStreamPath(photo.getFilename()).getAbsolutePath();
            File file = new File(path);
            file.delete();
        }
        mCrime.setPhoto(null);
        mPhotoView.setImageDrawable(null);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE,MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

    private boolean isIntentActionExist(String action) {
        PackageManager pm = getActivity().getPackageManager();
        Intent i = new Intent(action);
        List<ResolveInfo> activities = pm.queryIntentActivities(i, 0);
        boolean isIntentSafe = activities.size() > 0;
        return isIntentSafe;
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onPause() {                    //对数据进行保存（放在onPause里最安全）
        super.onPause();
        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }
}
