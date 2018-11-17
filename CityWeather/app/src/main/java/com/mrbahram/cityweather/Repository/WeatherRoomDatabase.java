package com.mrbahram.cityweather.Repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.mrbahram.cityweather.Models.WeatherModel;

/**
 * Creates WeatherRoomDatabase and Make the WeatherRoomDatabase a singleton
 * to prevent having multiple instances of the database opened at the same time.
 * Database name is weatherDB.
 */
@Database(entities = {WeatherModel.class}, version = 12)
public abstract class WeatherRoomDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
    private static volatile WeatherRoomDatabase INSTANCE;
    static WeatherRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeatherRoomDatabase.class, "WeatherDB")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
