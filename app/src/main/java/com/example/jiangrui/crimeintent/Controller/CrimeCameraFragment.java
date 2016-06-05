package com.example.jiangrui.crimeintent.Controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.jiangrui.crimeintent.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by Jiang Rui on 2016/6/4.
 */
public class CrimeCameraFragment extends Fragment {
    private static final String TAG = "CrimeCameraFragment";
    public static final String EXTRA_PHOTO_FILENAME = "com.example.jiangrui.crimeintent.photo_filename";
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private View mProgressBarContainer;

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mProgressBarContainer.setVisibility(View.VISIBLE);
        }
    };
    private Camera.PictureCallback mJPEGCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            boolean success = true;
            String fileName = UUID.randomUUID().toString() + ".jpg";
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                fileOutputStream.write(data);
            } catch (Exception e) {
                Log.e(TAG, "Error writing to file " + fileName, e);
                success = false;
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error closing file " + fileName, e);
                    success = false;
                }
            }
            if (success){
                Intent i = new Intent();
                i.putExtra(EXTRA_PHOTO_FILENAME,fileName);
                getActivity().setResult(Activity.RESULT_OK,i);
            }
            else{
                getActivity().setResult(Activity.RESULT_CANCELED);
            }
            getActivity().finish();
        }
    };

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_camera, container, false);
        Button takePictureButton = (Button) view.findViewById(R.id.crime_camera_takePictureButton);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null)
                   mCamera.takePicture(mShutterCallback,null,mJPEGCallBack);
//                    getActivity().finish();
            }
        });

        mSurfaceView = (SurfaceView) view.findViewById(R.id.crime_camera_surfaceView);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                /*
                Tell the camera to use this surface as its preview area
                包含SurfaceView的视图层级被放到屏幕上时调用该方法。也是Surface与其客户端进行关联的地方
                 */
                try {
                    if (mCamera != null)
                        mCamera.setPreviewDisplay(holder);       //该方法用来连接Camera与Surface
                } catch (IOException e) {
                    Log.e(TAG, "Error setting up preview display", e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null)
                    return;
                //The surface has changed size;update the camera preview size
                Camera.Parameters parameters = mCamera.getParameters();
                /*
                getSupportedPreviewSizes()方法返回android.hardware.Camera.Size类实例的一个列表
                每个实例封装了一个具体的图片宽高尺寸
                 */
                Camera.Size s = getBestSupportSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(s.width, s.height);
                s = getBestSupportSize(parameters.getSupportedPictureSizes(), width, height);
                parameters.setPictureSize(s.width, s.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();                     //该方法用来在Surface上绘制帧
                } catch (Exception e) {
                    Log.e(TAG, "Could not start preview", e);
                    mCamera.release();
                    mCamera = null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //We can no longer display on this surface,so stop the preview
                if (mCamera != null) {
                    mCamera.stopPreview();                      //该方法用来停止在Surface上绘制帧
                }
            }
        });

        mProgressBarContainer = view.findViewById(R.id.crime_camera_progressContainer);
        mProgressBarContainer.setVisibility(View.INVISIBLE);

        return view;
    }

    @SuppressWarnings("deprecation")
    private Camera.Size getBestSupportSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

    @TargetApi(9)
    @Override
    @SuppressWarnings("deprecation")             //取消删除线
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}