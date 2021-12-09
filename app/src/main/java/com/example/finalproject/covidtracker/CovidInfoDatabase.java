package com.example.finalproject.covidtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class CovidInfoDatabase extends SQLiteOpenHelper {

    private final String TABLE_NAME = "covidInformation";
    private final String COL_DATE = "date";
    private final String COL_CH_CASES = "changeCases";
    private final String COL_CH_FATALITIES = "changeFatalities";
    private final String COL_CH_RECOVERIES = "changeRecoveries";
    private final String COL_CH_HOSPITALIZATION = "changeHospitalization";
    private final String COL_CH_VACCINATION = "changeVaccination";
    private final String COL_TT_CASES = "totalCases";
    private final String COL_TT_FATALITIES = "totalFatalities";
    private final String COL_TT_RECOVERIES = "totalRecoveries";
    private final String COL_TT_HOSPITALIZATION = "totalHospitalization";
    private final String COL_TT_VACCINATION = "totalVaccination";

    public CovidInfoDatabase(@Nullable Context context){
        super(context, "CovidInfo", null, 1);
    }

    /**
     * This function is used to create the Database
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+"(" +
                COL_DATE +" TEXT PRIMARY KEY, "+
                COL_CH_CASES +" INTEGER, "+
                COL_CH_FATALITIES +" INTEGER, "+
                COL_CH_RECOVERIES +" INTEGER, "+
                COL_CH_HOSPITALIZATION +" INTEGER, "+
                COL_CH_VACCINATION +" INTEGER, "+
                COL_TT_CASES +" INTEGER, "+
                COL_TT_FATALITIES +" INTEGER, "+
                COL_TT_RECOVERIES +" INTEGER, "+
                COL_TT_HOSPITALIZATION + " INTEGER, "+
                COL_TT_VACCINATION +" INTEGER "+
                ")");
    }

    /**
     * This function drops the table if the table is already exists and creates the another one
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * @param info
     */
    public void addCovidInfo(CovidInfo info){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, info.getDate());
        contentValues.put(COL_CH_CASES, info.getChangeCases());
        contentValues.put(COL_CH_FATALITIES, info.getChangeFatalities());
        contentValues.put(COL_CH_RECOVERIES, info.getChangeRecoveries());
        contentValues.put(COL_CH_HOSPITALIZATION, info.getChangeHospitalization());
        contentValues.put(COL_CH_VACCINATION, info.getChangeVaccinations());
        contentValues.put(COL_TT_CASES, info.getTotalCases());
        contentValues.put(COL_TT_FATALITIES, info.getTotalFatalities());
        contentValues.put(COL_TT_RECOVERIES, info.getTotalRecoveries());
        contentValues.put(COL_TT_HOSPITALIZATION, info.getTotalHospitalization());
        contentValues.put(COL_TT_VACCINATION, info.getTotalVaccinations());

        /**
         * It inserts the data into the database
         */
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(TABLE_NAME, null, contentValues);
    }

    public List<CovidInfo> getAllSavedInfo(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{COL_DATE, COL_CH_CASES, COL_CH_FATALITIES, COL_CH_RECOVERIES, COL_CH_HOSPITALIZATION, COL_CH_VACCINATION, COL_TT_CASES, COL_TT_FATALITIES, COL_TT_RECOVERIES,COL_TT_HOSPITALIZATION, COL_TT_VACCINATION},
                null, null, null, null, null);

        List<CovidInfo> infoList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {

                CovidInfo covidInfo = new CovidInfo();
                covidInfo.setDate(cursor.getString(0));
                covidInfo.setChangeCases(cursor.getInt(1));
                covidInfo.setChangeFatalities(cursor.getInt(2));
                covidInfo.setChangeRecoveries(cursor.getInt(3));
                covidInfo.setChangeHospitalization(cursor.getInt(4));
                covidInfo.setChangeVaccinations(cursor.getInt(5));
                covidInfo.setTotalCases(cursor.getInt(6));
                covidInfo.setTotalFatalities(cursor.getInt(7));
                covidInfo.setTotalRecoveries(cursor.getInt(8));
                covidInfo.setTotalHospitalization(cursor.getInt(9));
                covidInfo.setTotalVaccinations(cursor.getInt(10));

                infoList.add(covidInfo);

            }while (cursor.moveToNext());
        }
        return infoList;
    }

    public int deleteCovidInfo(CovidInfo covidInfo){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME,
                COL_DATE+" = ? ",
                new String[]{covidInfo.getDate()});
    }

    public boolean isCovidInfoSaved(CovidInfo info){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_NAME,
                new String[]{COL_DATE},
                null, null, null, null, null);

        return cursor.getCount() > 0 ?true:false;
    }
}
