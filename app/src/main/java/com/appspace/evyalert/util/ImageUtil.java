package com.appspace.evyalert.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.google.firebase.crash.FirebaseCrash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by siwaweswongcharoen on 8/16/2016 AD.
 */
public class ImageUtil {
    public static Bitmap scaleTo(Bitmap originalImage, ImageView view) {
        int width = view.getMeasuredWidth();
        int height = view.getMeasuredHeight();
        Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = width / originalWidth;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(originalImage, transformation, paint);
        return background;
    }

    public static File resizeDown(File originalFile, File destinationFile) {
        Bitmap bMap = BitmapFactory.decodeFile(originalFile.getAbsolutePath());
        // accept scale is w<=800 or h<=1000 use h
        if (bMap.getHeight() <= 1000)
            return null;
        float newHeight = 1000;
        float newWidth = bMap.getWidth() * (newHeight * 100 / bMap.getHeight()) / 100;
        LoggerUtils.log2D("size", "ori:"+bMap.getWidth()+", "+bMap.getHeight());
        LoggerUtils.log2D("size", "des:"+newWidth+", "+newHeight);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bMap, (int)newWidth, (int)newHeight, false);
        try {
            FileOutputStream out = new FileOutputStream(destinationFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.close();
        } catch (IOException e) {
            FirebaseCrash.report(e);
        }
        return destinationFile;
    }
}
