package com.pets.app.widgets;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/**
 * Created by BAJIRAO on 05 January 2018.
 */
public class RoundedImage {

    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        Bitmap output = null;
        if (scaleBitmapImage.getWidth() > scaleBitmapImage.getHeight()) {
            output = Bitmap.createBitmap(scaleBitmapImage.getHeight(),
                    scaleBitmapImage.getHeight(), Config.ARGB_8888);

            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, scaleBitmapImage.getWidth(),
                    scaleBitmapImage.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            canvas.drawCircle(scaleBitmapImage.getHeight() / 2,
                    scaleBitmapImage.getHeight() / 2,
                    scaleBitmapImage.getHeight() / 2, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(scaleBitmapImage, rect, rect, paint);
            // Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
            // return _bmp;
        } else {
            try {

                output = Bitmap.createBitmap(scaleBitmapImage.getWidth(),
                        scaleBitmapImage.getHeight(), Config.ARGB_8888);

                Canvas canvas = new Canvas(output);
                final int color = 0xff424242;
                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, scaleBitmapImage.getWidth(),
                        scaleBitmapImage.getHeight());

                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(color);
                // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
                canvas.drawCircle(scaleBitmapImage.getWidth() / 2,
                        scaleBitmapImage.getHeight() / 2,
                        scaleBitmapImage.getWidth() / 2, paint);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                canvas.drawBitmap(scaleBitmapImage, rect, rect, paint);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return output;
    }
}

