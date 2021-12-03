package com.example.finalproject.owlbotdictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OwlbotDicitonaryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "owlbotDictionary";
    private static final String TABLE_NAME = "definitions";
    private static final String COL_ID = "id";
    private static final String COL_WORD = "word";
    private static final String COL_PRONUNCIATION = "pronunciation";
    private static final String COL_TYPE = "type";
    private static final String COL_DEFINITION = "definition";
    private static final String COL_EXAMPLE = "example";


    public OwlbotDicitonaryDatabase(Context context) {
        super(context, DATABASE_NAME,null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+" (" +
                COL_ID + " TEXT PRIMARY KEY, "+
                COL_WORD +" TEXT," +
                COL_PRONUNCIATION+" TEXT," +
                COL_TYPE+" TEXT," +
                COL_DEFINITION+" TEXT," +
                COL_EXAMPLE+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertDefinition(Word word, Definition definition) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, word.getWord()+definition.getDefinition());
        contentValues.put(COL_WORD, word.getWord());
        contentValues.put(COL_PRONUNCIATION, word.getPronunciation());
        contentValues.put(COL_DEFINITION, definition.getDefinition());
        contentValues.put(COL_EXAMPLE, definition.getExample());
        contentValues.put(COL_TYPE, definition.getType());

        //getting database as Writable to insert the data
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(TABLE_NAME, null, contentValues);
    }

    public int deleteDefinition(Word word, Definition definition){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COL_ID+" = ? ",
                new String[]{word.getWord()+definition.getDefinition()});
    }

    public List<Word> getSavedDefinition(){
        List<Word> words = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{COL_ID, COL_WORD, COL_PRONUNCIATION, COL_TYPE, COL_DEFINITION, COL_EXAMPLE},
                null, null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                Word word = new Word();
                word.setId(cursor.getString(0));
                word.setWord(cursor.getString(1));
                word.setPronunciation(cursor.getString(2));
                Definition definition = new Definition();
                definition.setType(cursor.getString(3));
                definition.setDefinition(cursor.getString(4));
                definition.setExample(cursor.getString(5));
                word.setDefinitions(Arrays.asList(definition));
                words.add(word);

            }while (cursor.moveToNext());
        }
        return words;
    }

}
