package com.mrbahram.cityweather.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.mrbahram.cityweather.Models.WeatherModel;
import com.mrbahram.cityweather.Repository.WeatherRepository;
import java.util.List;

/**
 * The WeatherViewModel class is a ViewModel class.
 * This class isolates and releases dependency of UI view from Repository solution.
 *
 */
public class WeatherViewModel extends AndroidViewModel {
    private WeatherRepository mRepository;
    private LiveData<List<WeatherModel>> mAllWeathers;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mRepository=new WeatherRepository(application);
        mAllWeathers=mRepository.getAllWeathersLiveData();
    }
    /**
     * Connects ViewModel to data source to notify when the data has been changed.
     * @return LiveData<List<WeatherModel>>
     */
    public LiveData<List<WeatherModel>> getAllWeathersLiveData(){
        return mAllWeathers;
    }
    /**
     *
     * @return List<WeatherModel> : all weathers object which are in the database
     */
    public List<WeatherModel> getAllWeathers(){
        return mRepository.getAllWeathers();
    }
    /**
     * inserts a new object to database
     * @param weather
     */
    public void insertWeather(WeatherModel weather){
        mRepository.insert(weather);
    }
    /**
     * delete a specific weather from database
     * @param weather
     */
    public void deleteWeather(WeatherModel weather){
        mRepository.deleteWeather(weather);
    }
    /**
     * finds an object with True defaultItem value
     * @return the object which the defaultItem value is true.
     */
    public WeatherModel getDefaultItem(){
        return  mRepository.getDefaultItem();
    }
    /**
     * sets true to the defaultItem value of specific item
     * @param cityName : primary key of the object
     */
    public void setAsDefaultItem(String cityName){
        mRepository.setAsDefaultItem(cityName);
    }
    /**
     * sets False to all defaultItem value
     */
    public void resetAllDefaultItem(){
        mRepository.resetAllDefaultItem();
    }
    /**
     * Update an object
     * @param weather
     */
    public void updateWeather(WeatherModel weather){
        mRepository.updateWeather(weather);
    }
    /**
     * update order of the list and save data in the correct order.
     * @param weathers
     */
    public  void setNewOrder(List<WeatherModel> weathers){
        mRepository.setNewOrder(weathers);
    }
    /**
     * searches and returns a specific object from database
     * @param cityName primary key of search object
     * @return the object with given primary key
     */
    public  WeatherModel getWeather(String cityName){
        return mRepository.getWeather(cityName);
    }
    /**
     * a new item will be added in the end of list.
     * @return
     */
    public  int getNewOrderNumber(){
        return  mRepository.getNewOrderNumber();
    }
}
