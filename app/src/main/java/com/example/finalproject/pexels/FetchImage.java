package com.example.finalproject.pexels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;

public class FetchImage extends AsyncTask<Void, Void, Bitmap> {

    String imageURL;
    ImageView imageView;
    ProgressBar progressBar;

    public FetchImage(String imageURL, ImageView imageView, ProgressBar progressBar) {
        this.imageURL = imageURL;
        this.imageView = imageView;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progressBar !=null)
            progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        URL url = null;
        try {
            url = new URL(imageURL);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {

            if(progressBar !=null)
                progressBar.setVisibility(View.GONE);
            if(imageView!=null)
                imageView.setImageBitmap(bitmap);
        }
    }
}
