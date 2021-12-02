package com.example.finalproject.carbondioxideinterface;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CarbonDioxideEmissionDatabase extends SQLiteOpenHelper {

    final String tableName = "carbonEmission";

    public CarbonDioxideEmissionDatabase(Context context){
        super(context, "carbonDioxideEmissionDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createQuery = "create table "+tableName+" (emissionId text primary key, " +
                "vehicleMaker text," +
                "vehicleModel text," +
                "vehicleYear text," +
                "distance real," +
                "distanceUnit real," +
                "carbonG real," +
                "carbonKg real," +
                "carbonLb real," +
                "carbonMt real)";
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
    }

    public void insertCarbonEmission(CarbonEmissionEstimate estimate) {
        //mapping all the values into the ContentValues,
        //column name as key and parameter as value
        ContentValues contentValues = new ContentValues();
        contentValues.put("emissionId", estimate.getEstimateId());
        contentValues.put("vehicleMaker", estimate.getVehicleMaker());
        contentValues.put("vehicleModel", estimate.getVehicleModel());
        contentValues.put("vehicleYear", estimate.getVehicleYear());
        contentValues.put("distance", estimate.getDistance());
        contentValues.put("distanceUnit", estimate.getDistanceUnit());
        contentValues.put("carbonG", estimate.getCarbonInG());
        contentValues.put("carbonKg", estimate.getCarbonInKg());
        contentValues.put("carbonLb", estimate.getCarbonInLb());
        contentValues.put("carbonMt", estimate.getCarbonInMt());

        //getting database as Writable to insert the data
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(tableName, null, contentValues);
    }

    public int deleteCarbonEmission(CarbonEmissionEstimate estimate){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName,
                 "emissionId = ? ",
                new String[]{estimate.getEstimateId()});
    }

    public List<CarbonEmissionEstimate> getSavedCarbonEmissions(){
        List<CarbonEmissionEstimate> emissionEstimates = new ArrayList<>();

        //Performing read operation on the database
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(tableName,
                new String[]{"emissionId", "vehicleMaker", "vehicleModel", "vehicleYear", "distance", "distanceUnit","carbonG","carbonKg","carbonLb","carbonMt"},
                null, null, null, null, null);

        //reading first data
        if(cursor.moveToFirst()){
            do {

                CarbonEmissionEstimate estimate = new CarbonEmissionEstimate();
                estimate.setEstimateId(cursor.getString(0));
                estimate.setVehicleMaker(cursor.getString(1));
                estimate.setVehicleModel(cursor.getString(2));
                estimate.setVehicleYear(cursor.getString(3));
                estimate.setDistance(cursor.getDouble(4));
                estimate.setDistanceUnit(cursor.getString(5));
                estimate.setCarbonInG(cursor.getDouble(6));
                estimate.setCarbonInKg(cursor.getDouble(7));
                estimate.setCarbonInLb(cursor.getDouble(8));
                estimate.setCarbonInMt(cursor.getDouble(9));

                emissionEstimates.add(estimate);

            }while (cursor.moveToNext()); //moving cursor to the next if the next data
            //is available
        }
        return emissionEstimates;
    }

    public CarbonEmissionEstimate getCarbonEmissionById(String emissionId){
        //Performing read operation on the database
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(tableName,
                new String[]{"emissionId", "vehicleMaker", "vehicleModel", "vehicleYear", "distance", "distanceUnit","carbonG","carbonKg","carbonLb","carbonMt"},
                "emissionId = ?", new String[]{emissionId}, null, null, null);

        //reading first data
        if(cursor.moveToFirst()){
            do {

                CarbonEmissionEstimate estimate = new CarbonEmissionEstimate();
                estimate.setEstimateId(cursor.getString(0));
                estimate.setVehicleMaker(cursor.getString(1));
                estimate.setVehicleModel(cursor.getString(2));
                estimate.setVehicleYear(cursor.getString(3));
                estimate.setDistance(cursor.getDouble(4));
                estimate.setDistanceUnit(cursor.getString(5));
                estimate.setCarbonInG(cursor.getDouble(6));
                estimate.setCarbonInKg(cursor.getDouble(7));
                estimate.setCarbonInLb(cursor.getDouble(8));
                estimate.setCarbonInMt(cursor.getDouble(9));

                return estimate;

            }while (cursor.moveToNext()); //moving cursor to the next if the next data
            //is available
        }
        return null;
    }
}
