package com.appspace.evyalert.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

/**
 * Created by siwaweswongcharoen on 8/16/2016 AD.
 */
public class ImageUtil {
    public static Bitmap scaleTo(Bitmap originalImage, ImageView view) {
        int width = view.getMeasuredWidth();
        int height = view. getMeasuredHeight();
        Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        float originalWidth = originalImage.getWidth();
        float originalHeight = originalImage.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = width/originalWidth;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale)/2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(originalImage, transformation, paint);
        return background;
    }
}
