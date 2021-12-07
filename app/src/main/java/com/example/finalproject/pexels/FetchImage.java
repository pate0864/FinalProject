package com.example.finalproject.pexels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;

public class FetchImage implements Runnable {

    String imageURL;
    OnImageFetchCompleteListener onImageFetchCompleteListener;

    public interface OnImageFetchCompleteListener{
        void onComplete(Bitmap bitmap);
    }

    public FetchImage(String imageURL,OnImageFetchCompleteListener onImageFetchCompleteListener) {
        this.imageURL = imageURL;
        this.onImageFetchCompleteListener = onImageFetchCompleteListener;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(imageURL);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            if(onImageFetchCompleteListener!=null)
                onImageFetchCompleteListener.onComplete(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
