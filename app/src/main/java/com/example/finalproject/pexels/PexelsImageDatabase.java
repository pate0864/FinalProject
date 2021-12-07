package com.example.finalproject.pexels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PexelsImageDatabase extends SQLiteOpenHelper {

    private final String TABLE = "images";
    private final String COLUMN_ID = "id";
    private final String COLUMN_HEIGHT = "height";
    private final String COLUMN_WIDTH = "width";
    private final String COLUMN_PHOTOGRAPHER = "photographer";
    private final String COLUMN_PHOTOGRAPHER_URL = "photographerUrl";
    private final String COLUMN_URL = "url";
    private final String COLUMN_MEDIUM_URL = "mediumUrl";
    private final String COLUMN_ORIGINAL_URL = "originalUrl";
    private final String COLUMN_SAVE_PATH = "savePath";

    public PexelsImageDatabase(@Nullable Context context) {
        super(context, "pexels", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_HEIGHT + " INTEGER, " +
                COLUMN_WIDTH + " INTEGER, " +
                COLUMN_PHOTOGRAPHER + " TEXT, " +
                COLUMN_PHOTOGRAPHER_URL + " TEXT, " +
                COLUMN_MEDIUM_URL + " TEXT, " +
                COLUMN_ORIGINAL_URL + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_SAVE_PATH + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
    }

    public List<PexelImage> getSavedImages() {
        List<PexelImage> images = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE,
                new String[]{COLUMN_ID, COLUMN_HEIGHT, COLUMN_WIDTH, COLUMN_PHOTOGRAPHER, COLUMN_PHOTOGRAPHER_URL, COLUMN_MEDIUM_URL, COLUMN_ORIGINAL_URL, COLUMN_URL, COLUMN_SAVE_PATH},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                PexelImage image = new PexelImage();
                image.setId(cursor.getInt(0));
                image.setHeight(cursor.getInt(1));
                image.setWidth(cursor.getInt(2));
                image.setPhotographer(cursor.getString(3));
                image.setPhotographerUrl(cursor.getString(4));
                image.setMediumUrl(cursor.getString(5));
                image.setOriginalUrl(cursor.getString(6));
                image.setUrl(cursor.getString(7));
                image.setSavePath(cursor.getString(8));
                images.add(image);
            } while (cursor.moveToNext());
        }

        return images;
    }

    public void deleteImage(PexelImage image) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE,
                COLUMN_ID + " = ? ",
                new String[]{String.valueOf(image.getId())});
    }

    public void saveImage(PexelImage image) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, image.getId());
        contentValues.put(COLUMN_HEIGHT, image.getHeight());
        contentValues.put(COLUMN_WIDTH, image.getWidth());
        contentValues.put(COLUMN_PHOTOGRAPHER, image.getPhotographer());
        contentValues.put(COLUMN_PHOTOGRAPHER_URL, image.getPhotographerUrl());
        contentValues.put(COLUMN_MEDIUM_URL, image.getMediumUrl());
        contentValues.put(COLUMN_ORIGINAL_URL, image.getOriginalUrl());
        contentValues.put(COLUMN_URL, image.getUrl());
        contentValues.put(COLUMN_SAVE_PATH, image.getSavePath());

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(TABLE, null, contentValues);
    }

}
