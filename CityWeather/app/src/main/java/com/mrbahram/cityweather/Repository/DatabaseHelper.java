package com.mrbahram.cityweather.Repository;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mrbahram.cityweather.Models.WeatherModel;


/**
 * creates SQLite database
 * Add,Delete and Update Weather information
 */
public  class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "WeatherDB.db";
    private static final String TABLE_NAME = "weather";
    private static final String COLUMN_CITY_NAME = "cityName";
    private static final String COLUMN_REGION = "region";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_LAT= "lat";
    private static final String COLUMN_LON = "lon";
    private static final String COLUMN_LOCAL_TIME="localTime";
    private static final String COLUMN_TEMPERATURE_C="temperatureC";
    private static final String COLUMN_WEATHER_CONDITION="weatherCondition";
    private static final String COLUMN_ICON="icon";
    private static final String COLUMN_FEELS_LIKE_C="feelsLikec";


    /**
     * initiate the super class
     * @param context
     */
    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME , null, 9);

    }

    /**
     * Creates the weather table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("query", "onCreate databasehelper ");
        String query="CREATE TABLE IF NOT EXISTS " +TABLE_NAME+" ( "+

                COLUMN_CITY_NAME+" text NOT NULL primary key, "+
                COLUMN_REGION+" text, "+
                COLUMN_COUNTRY+" text, "+
                COLUMN_LAT+" double, "+
                COLUMN_LON+" double, "+
                COLUMN_LOCAL_TIME+" text, "+
                COLUMN_TEMPERATURE_C+" double, "+
                COLUMN_WEATHER_CONDITION+" text, "+
                COLUMN_ICON+" text ,"+
                COLUMN_FEELS_LIKE_C+" double"+
                " )";
        Log.d("query", "query: "+query);
        db.execSQL(query);
    }

    /**
     * Upgrade the weather table
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    /**
     * Add a Weather object into database
     * @param data is a Weather object.
     * @return true if insert query was successful.
     */
    public boolean add (WeatherModel data) {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CITY_NAME, data.getCityName());
        contentValues.put(COLUMN_REGION,data.getRegion() );
        contentValues.put(COLUMN_COUNTRY, data.getCountry());
        contentValues.put(COLUMN_LAT, data.getLat());
        contentValues.put(COLUMN_LON, data.getLon());
        contentValues.put(COLUMN_LOCAL_TIME, data.getLocalTime());
        contentValues.put(COLUMN_TEMPERATURE_C, data.getTemperature_C());
        contentValues.put(COLUMN_WEATHER_CONDITION, data.getWeatherCondition());
        contentValues.put(COLUMN_ICON, data.getIcon());
        contentValues.put(COLUMN_FEELS_LIKE_C, data.getFeelsLike_c());


        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    /**
     * Remove a city name from database
     * @param cityName is a value for remove a related row from database.
     * @return the number of rows deleted as an integer
     */
    public Integer remove (String cityName) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_CITY_NAME+" = ? ",
                new String[] { cityName });
    }

    /**
     * delete all rows
     */
    private void removeAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    /**
     * In this application scenario, the first row is
     * the default value for presenting on the screen.
     * @return a first element of the weather table.
     */
    public WeatherModel getFirst(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();

        if(res.isFirst() ){
            WeatherModel item=new WeatherModel();
            item.setCityName(res.getString(res.getColumnIndex(COLUMN_CITY_NAME)));
            item.setRegion(res.getString(res.getColumnIndex(COLUMN_REGION)));
            item.setCountry(res.getString(res.getColumnIndex(COLUMN_COUNTRY)));
            item.setLat(res.getDouble(res.getColumnIndex(COLUMN_LAT)));
            item.setLon(res.getDouble(res.getColumnIndex(COLUMN_LON)));
            item.setLocalTime(res.getString(res.getColumnIndex(COLUMN_LOCAL_TIME)));
            item.setTemperature_C(res.getDouble(res.getColumnIndex(COLUMN_TEMPERATURE_C)));
            item.setWeatherCondition(res.getString(res.getColumnIndex(COLUMN_WEATHER_CONDITION)));
            item.setIcon(res.getString(res.getColumnIndex(COLUMN_ICON)));
            item.setFeelsLike_c(res.getDouble(res.getColumnIndex(COLUMN_FEELS_LIKE_C)));

            res.close();
            return item;
        }else{
            res.close();
            return null;
        }

    }

    /**
     *
     * @return all weather objects
     */
    public ArrayList<WeatherModel> getAll() {
        ArrayList<WeatherModel> array_list = new ArrayList<WeatherModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            WeatherModel item=new WeatherModel();
            item.setCityName(res.getString(res.getColumnIndex(COLUMN_CITY_NAME)));
            item.setRegion(res.getString(res.getColumnIndex(COLUMN_REGION)));
            item.setCountry(res.getString(res.getColumnIndex(COLUMN_COUNTRY)));
            item.setLat(res.getDouble(res.getColumnIndex(COLUMN_LAT)));
            item.setLon(res.getDouble(res.getColumnIndex(COLUMN_LON)));
            item.setLocalTime(res.getString(res.getColumnIndex(COLUMN_LOCAL_TIME)));
            item.setTemperature_C(res.getDouble(res.getColumnIndex(COLUMN_TEMPERATURE_C)));
            item.setWeatherCondition(res.getString(res.getColumnIndex(COLUMN_WEATHER_CONDITION)));
            item.setIcon(res.getString(res.getColumnIndex(COLUMN_ICON)));
            item.setFeelsLike_c(res.getDouble(res.getColumnIndex(COLUMN_FEELS_LIKE_C)));

            array_list.add(item);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    /**
     * update a row
     * @param data is a value to update
     */
    public void update(WeatherModel data) {
        SQLiteDatabase db = this.getWritableDatabase();

// New value for one column

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CITY_NAME, data.getCityName());
        contentValues.put(COLUMN_REGION,data.getRegion() );
        contentValues.put(COLUMN_COUNTRY, data.getCountry());
        contentValues.put(COLUMN_LAT, data.getLat());
        contentValues.put(COLUMN_LON, data.getLon());
        contentValues.put(COLUMN_LOCAL_TIME, data.getLocalTime());
        contentValues.put(COLUMN_TEMPERATURE_C, data.getTemperature_C());
        contentValues.put(COLUMN_WEATHER_CONDITION, data.getWeatherCondition());
        contentValues.put(COLUMN_ICON, data.getIcon());
        contentValues.put(COLUMN_FEELS_LIKE_C, data.getFeelsLike_c());

// Which row to update, based on the title
        String selection = COLUMN_CITY_NAME + " = ?";
        String[] selectionArgs = { data.getCityName() };

        int count = db.update(
                TABLE_NAME,
                contentValues,
                selection,
                selectionArgs);
    }

    /**
     * change ordering of the list
     * @param data is a new ordered list.
     */
    public void updateOrder(ArrayList<WeatherModel> data) {

        removeAll();
        for (WeatherModel item:data) {
            add(item);
        }

    }

}
