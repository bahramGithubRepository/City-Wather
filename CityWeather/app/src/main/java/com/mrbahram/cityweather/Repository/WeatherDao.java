package com.mrbahram.cityweather.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.mrbahram.cityweather.Models.WeatherModel;
import java.util.List;
import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * DAO(Data Access Object) interface
 */
@Dao
public interface WeatherDao {
    /**
     * inserts a new object to database
     * @param weather
     */
    @Insert(onConflict = IGNORE)
    void insertWeather(WeatherModel weather);

    /**
     * delete a specific weather from database
     * @param weather
     */
    @Delete
    void deleteWeather(WeatherModel weather);

    /**
     * update all weathers information
     * @param weathers
     */
    @Update
    void updateAllWeathers(List<WeatherModel> weathers);

    /**
     * finds an object with True defaultItem value
     * @return the object which the defaultItem value is true.
     */
    @Query("select * from weather where defaultItem=1")
    WeatherModel getDefaultItem();

    /**
     * sets true to the defaultItem value of specific item
     * @param city: primary key of the object
     */
    @Query("update weather set defaultItem=1 where cityName = :city")
    void setAsDefaultItem(String city);

    /**
     * sets False to all defaultItem value
     */
    @Query("update weather set defaultItem=0")
    void resetAllDefaultItemValue();

    /**
     * Connects ViewModel to data source to notify when the data has been changed.
     * @return LiveData<List<WeatherModel>>
     */
    @Query("select * from weather ORDER BY orderValue ASC")
    LiveData<List<WeatherModel>> getAllWeathersLiveData();

    /**
     *
     * @return all weathers object which are in the database
     */
    @Query("select * from weather ORDER BY orderValue ASC")
    List<WeatherModel> getAllWeathers();

    /**
     * searches and returns a specific object from database
     * @param city primary key of search object
     * @return the object with given primary key
     */
    @Query("select * from weather where cityName like :city")
    WeatherModel getWeather(String city);

    /**
     * Update an object
     * @param weatherModel
     */
    @Update(onConflict = IGNORE)
    void updateWeather(WeatherModel weatherModel);

    /**
     * a new item will be added in the end of list.
     * @return
     */
    @Query("select count(*)+1 from weather")
    int getNewOrderNumber();

}
