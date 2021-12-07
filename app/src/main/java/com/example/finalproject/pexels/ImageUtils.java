package com.example.finalproject.pexels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

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
}
