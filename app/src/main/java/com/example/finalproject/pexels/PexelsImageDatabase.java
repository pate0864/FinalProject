package com.example.finalproject.pexels;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PexelsImageDatabase extends SQLiteOpenHelper {

    private final String TABLE = "images";

    public PexelsImageDatabase(@Nullable Context context) {
        super(context, "pexels" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE);
    }

    public List<PexelImage> getSavedImages(){
        List<PexelImage> images = new ArrayList<>();
        return images;
    }

}
