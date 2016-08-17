package com.appspace.evyalert.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.widget.ImageView;

import com.appspace.appspacelibrary.util.LoggerUtils;
import com.google.firebase.crash.FirebaseCrash;

import java.io.ByteArrayOutputStream;
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

    public static Uri rotateImage(Uri imageUri, int degree) {
        Bitmap img = BitmapFactory.decodeFile(imageUri.getPath());
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();

        File f = new File(imageUri.getPath());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        rotatedImg.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageUri;
    }

    public static int checkImageOrientation(Uri imageUri) {
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(imageUri.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                case ExifInterface.ORIENTATION_NORMAL:
                    return 0;
                default:
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
