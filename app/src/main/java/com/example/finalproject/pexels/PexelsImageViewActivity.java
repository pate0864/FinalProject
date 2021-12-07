package com.example.finalproject.pexels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.R;

import java.util.concurrent.ExecutionException;

public class PexelsImageViewActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    TextView textViewPhotographer, textViewHeight, textViewWidth;
    ImageView imageView;
    Button buttonPhotographer, buttonOriginalImage, buttonOperation;
    ProgressBar progressImage;

    PexelImage pexelImage;
    boolean savedImage = false;
    PexelsImageDatabase database;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pexels_image_view);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.project_pexels);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewHeight = findViewById(R.id.textViewHeight);
        textViewPhotographer = findViewById(R.id.textViewPhotographer);
        textViewWidth = findViewById(R.id.textViewWidth);
        imageView = findViewById(R.id.imageViewPhoto);
        buttonOperation = findViewById(R.id.buttonOperation);
        buttonOriginalImage = findViewById(R.id.buttonOriginalImage);
        buttonPhotographer = findViewById(R.id.buttonPhotographer);
        progressImage = findViewById(R.id.progressImage);

        database = new PexelsImageDatabase(this);

        pexelImage = (PexelImage) getIntent().getSerializableExtra("image");

        textViewPhotographer.setText(pexelImage.getPhotographer());
        textViewWidth.setText(""+pexelImage.getWidth());
        textViewHeight.setText(""+pexelImage.getHeight());

        if(pexelImage.getSavePath() != null) {
            buttonOperation.setText(getString(R.string.pexels_delete));
            savedImage = true;
        }
        else {
            buttonOperation.setText(getString(R.string.pexels_save));
            savedImage = false;
        }

        if(savedImage) {
            bitmap = ImageUtils.getImageFromGallery(pexelImage.getSavePath(), this);
            imageView.setImageBitmap(bitmap);
        }
        else {
            try {
                bitmap = new FetchImage(pexelImage.getOriginalUrl(), imageView, progressImage).execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        buttonOperation.setOnClickListener(this::onClick);
        buttonOriginalImage.setOnClickListener(this::onClick);
        buttonPhotographer.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonOperation:{
                if(savedImage)
                {
                    database.deleteImage(pexelImage);
                    Toast.makeText(this, "Image Deleted", Toast.LENGTH_SHORT).show();
                    buttonOperation.setText(getString(R.string.pexels_save));
                    savedImage = false;
                }else{
                    pexelImage.setSavePath(ImageUtils.saveImageToGallery(bitmap, String.valueOf(pexelImage.getId()),this));
                    database.saveImage(pexelImage);
                    Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
                    buttonOperation.setText(getString(R.string.pexels_delete));
                    savedImage = true;
                }
            }break;
            case R.id.buttonOriginalImage:{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pexelImage.getOriginalUrl()));
                startActivity(intent);
            }break;
            case R.id.buttonPhotographer:{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pexelImage.getPhotographerUrl()));
                startActivity(intent);
            }break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}