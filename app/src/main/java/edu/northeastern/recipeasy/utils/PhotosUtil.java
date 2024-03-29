package edu.northeastern.recipeasy.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.InputStream;

public class PhotosUtil {
    public static Uri makeImageUri(Context applicationContext) {
        File imageFile = new File(applicationContext.getFilesDir(), "pic.jpg");
        return FileProvider.getUriForFile(
                applicationContext,
                "edu.northeastern.recipeasy.fileProvider",
                imageFile
        );
    }

    public static Bitmap uriToBitmap(Uri uri, ContentResolver resolver) {
        try {
            InputStream inputStream = resolver.openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ExifInterface exif = new ExifInterface(resolver.openInputStream(uri));
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            Matrix matrix = new Matrix();
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.setRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.setRotate(180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.setRotate(270);
                    break;
                default:
                    break;
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            inputStream.close();
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }


}
