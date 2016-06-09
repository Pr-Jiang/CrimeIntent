package com.example.jiangrui.crimeintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

/**
 * Created by Jiang Rui on 2016/6/5.
 */
public class PictureUtils {
    /*
    Get a BitmapDrawable from a local file that is scaled down
    to fit the current Window size.
     */
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaledDrawable(Activity activity, String path) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();

        //Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;                  //只解析out...fields,bitmap不加载到内存
        BitmapFactory.decodeFile(path,options);             //加载bitmap对象但只返回out...fields
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;

        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcHeight > destHeight) {
                inSampleSize = Math.round(srcHeight / destHeight);
            } else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
            while ((srcHeight / inSampleSize > destHeight) || (srcWidth / inSampleSize) > destWidth) {
                inSampleSize *= 2;
            }
        }
        options = new BitmapFactory.Options();           //默认options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);   //加载 inSampleSize 比率缩小的bitmap对象
        return new BitmapDrawable(activity.getResources(), bitmap);
    }

    public static void cleanImageView(ImageView imageView) {
        if (!(imageView.getDrawable() instanceof BitmapDrawable))
            return;
        //Clean up the view's image for the sake of memory
        BitmapDrawable b = (BitmapDrawable) imageView.getDrawable();
        b.getBitmap().recycle();
        imageView.setImageDrawable(null);
    }

}
