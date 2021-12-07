package com.example.finalproject.pexels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUtils {
    public static Bitmap getImageFromGallery(String path, Context context) {
        final InputStream imageStream;
        try {
            imageStream = context.getContentResolver().openInputStream(Uri.parse(path));
            return BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveImageToGallery(Bitmap bitmap, String id, Context context){
        return MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, id, "");
    }
}
