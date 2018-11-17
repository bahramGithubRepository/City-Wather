package com.mrbahram.cityweather.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.mrbahram.cityweather.Models.WeatherModel;


import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Weather Repository class.
 * Runs all WeatherDao function in a separate asyncTask to avoid locking mainUI thread.
 */
public class WeatherRepository {
    private WeatherDao mWeatherDao;
    private LiveData<List<WeatherModel>> mAllWeathers;

    public WeatherRepository(Application application) {
        //Creates instance of the database
        WeatherRoomDatabase db=WeatherRoomDatabase.getDatabase(application);
        mWeatherDao=db.weatherDao();
        mAllWeathers=mWeatherDao.getAllWeathersLiveData();
    }

    /**
     * Connects ViewModel to data source to notify when the data has been changed.
     * @return LiveData<List<WeatherModel>>
     */
    public LiveData<List<WeatherModel>> getAllWeathersLiveData() {
        return mAllWeathers;
    }

    /**
     * inserts a new object to database
     * @param weather
     */
    public void insert(WeatherModel weather){
        new InsertAsyncTask(mWeatherDao).execute(weather);
    }
    /**
     * delete a specific weather from database
     * @param weather
     */
    public void deleteWeather(WeatherModel weather){
        new DeleteAsyncTask(mWeatherDao).execute(weather);

    }
    /**
     * finds an object with True defaultItem value
     * @return the object which the defaultItem value is true.
     */
    public WeatherModel getDefaultItem(){
        FirstWeatherAsyncTask firstWeatherAsyncTask=new FirstWeatherAsyncTask(mWeatherDao);
        firstWeatherAsyncTask.execute();
        try {
            return firstWeatherAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        //return mWeatherDao.getDefaultItem();
    }
    /**
     * sets true to the defaultItem value of specific item
     * @param cityName : primary key of the object
     */
    public void setAsDefaultItem(String cityName){
        new SetDefaultItemAsyncTask(mWeatherDao).execute(cityName);
    }
    /**
     * sets False to all defaultItem value
     */
    public void resetAllDefaultItem(){
        new ResetAllDefaultItemAsyncTask(mWeatherDao).execute();
    }
    /**
     *
     * @return List<WeatherModel> : all weathers object which are in the database
     */
    public List<WeatherModel> getAllWeathers(){
        GetALLWeatherAsyncTask all=new GetALLWeatherAsyncTask(mWeatherDao);
        all.execute();
        try {
            return all.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        //return mWeatherDao.getDefaultItem();
    }
    /**
     * Update an object
     * @param weather
     */
    public void updateWeather(WeatherModel weather){
        new UpdateAsyncTask(mWeatherDao).execute(weather);
    }
    /**
     * update order of the list and save data in the correct order.
     * @param weathers
     */
    public  void setNewOrder(List<WeatherModel> weathers){
        new UpdateOrderAsyncTask(mWeatherDao).execute(weathers);
    }
    /**
     * a new item will be added in the end of list.
     * @return
     */
    public int getNewOrderNumber(){
        NewOrderNumberAsyncTask newOrderNumberAsyncTask=new NewOrderNumberAsyncTask(mWeatherDao);
        newOrderNumberAsyncTask.execute();
        try {
            return newOrderNumberAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return -1;
        }

        //return mWeatherDao.getNewOrderNumber();
    }
    /**
     * searches and returns a specific object from database
     * @param cityName primary key of search object
     * @return the object with given primary key
     */
    public  WeatherModel getWeather(String cityName){
        GetWeatherAsyncTask getWeatherAsyncTask=new GetWeatherAsyncTask(mWeatherDao);
        getWeatherAsyncTask.execute(cityName);
        try {
            return getWeatherAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    //===============Private function=======================
    private static class InsertAsyncTask extends AsyncTask<WeatherModel, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        InsertAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WeatherModel... params) {
            mAsyncTaskDao.insertWeather(params[0]);
            return null;
        }
    }
    private static class SetDefaultItemAsyncTask extends AsyncTask<String, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        SetDefaultItemAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.setAsDefaultItem(params[0]);
            return null;
        }
    }
    private static class ResetAllDefaultItemAsyncTask extends AsyncTask<Void, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        ResetAllDefaultItemAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.resetAllDefaultItemValue();
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<WeatherModel, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        DeleteAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WeatherModel... params) {
            mAsyncTaskDao.deleteWeather(params[0]);
            return null;
        }
    }
    private static class FirstWeatherAsyncTask extends AsyncTask<Void, Void, WeatherModel> {

        private WeatherDao mAsyncTaskDao;



        FirstWeatherAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected WeatherModel doInBackground(Void... voids) {

            return mAsyncTaskDao.getDefaultItem();
        }


    }
    private static class UpdateAsyncTask extends AsyncTask<WeatherModel, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        UpdateAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WeatherModel... params) {
            mAsyncTaskDao.updateWeather(params[0]);
            return null;
        }
    }
    private static class GetALLWeatherAsyncTask extends AsyncTask<Void, Void, List<WeatherModel>> {

        private WeatherDao mAsyncTaskDao;


        GetALLWeatherAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<WeatherModel> doInBackground(Void... voids) {

            return mAsyncTaskDao.getAllWeathers();
        }
    }
    private static class NewOrderNumberAsyncTask extends AsyncTask<Void, Void, Integer> {

        private WeatherDao mAsyncTaskDao;
        NewOrderNumberAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            return mAsyncTaskDao.getNewOrderNumber();
        }



    }
    private static class UpdateOrderAsyncTask extends AsyncTask<List<WeatherModel>, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        UpdateOrderAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final List<WeatherModel>... params) {
            mAsyncTaskDao.updateAllWeathers(params[0]);
            return null;
        }
    }

    private static class GetWeatherAsyncTask extends AsyncTask<String, Void, WeatherModel> {

        private WeatherDao mAsyncTaskDao;


        GetWeatherAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected WeatherModel doInBackground(String... params) {


            WeatherModel weatherModel=mAsyncTaskDao.getWeather(params[0]);

            return weatherModel;
        }


    }
}
