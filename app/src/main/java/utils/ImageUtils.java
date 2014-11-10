package utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by bastien on 11/10/14.
 */
public class ImageUtils {

    static public Bitmap getResizedBitmap(Bitmap bm, int resolution) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        if (height > resolution && width > resolution) {

            float scale = ((float) resolution) / Math.min(width, height);
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scale, scale);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
            return resizedBitmap;
        } else {
            return bm;
        }
    }

    static public Bitmap makeSquareBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()){

            return Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        }else{

            return Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }
    }

    static public Bitmap mirrorBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    static public Bitmap makeThumb(Bitmap bitmap) {
        return mirrorBitmap(makeSquareBitmap(getResizedBitmap(bitmap,
                                                                   Constants.PROFILE_PICTURE_RES)));
    }
}
