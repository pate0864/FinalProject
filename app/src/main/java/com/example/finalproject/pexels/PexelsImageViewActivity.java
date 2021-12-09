package com.example.finalproject.pexels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
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
import java.util.concurrent.Executors;

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
            progressImage.setVisibility(View.VISIBLE);
            Executors.newSingleThreadExecutor().execute(new FetchImage(pexelImage.getOriginalUrl(), image->{
                runOnUiThread(()->{
                    bitmap = image;
                    progressImage.setVisibility(View.GONE);
                    imageView.setImageBitmap(bitmap);
                });
            }));
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
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Delete Image")
                            .setMessage("Are you sure you want to delete?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    database.deleteImage(pexelImage);
                                    Toast.makeText(PexelsImageViewActivity.this, "Image Deleted", Toast.LENGTH_SHORT).show();
                                    buttonOperation.setText(getString(R.string.pexels_save));
                                    savedImage = false;
                                }
                            })
                            .setCancelable(false);

                    dialog.create().show();

                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle("Save Image")
                            .setMessage("Are you sure you want to save?")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    pexelImage.setSavePath(ImageUtils.saveImageToGallery(bitmap, String.valueOf(pexelImage.getId()),PexelsImageViewActivity.this));
                                    database.saveImage(pexelImage);
                                    Toast.makeText(PexelsImageViewActivity.this, "Image Saved", Toast.LENGTH_SHORT).show();
                                    buttonOperation.setText(getString(R.string.pexels_delete));
                                    savedImage = true;
                                }
                            })
                            .setCancelable(false);

                    dialog.create().show();

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