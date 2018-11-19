package com.mrbahram.citywather;

import android.app.Application;
import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.mrbahram.cityweather.Models.WeatherModel;
import com.mrbahram.cityweather.Repository.WeatherDao;
import com.mrbahram.cityweather.Repository.WeatherRepository;
import com.mrbahram.cityweather.Repository.WeatherRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class WeatherDaoTests {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private WeatherDao mWeatherDao;
    private WeatherRoomDatabase mWeatherRoomDatabase;

    @Before
    public void createDB(){
        Context context= InstrumentationRegistry.getTargetContext();
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        mWeatherRoomDatabase=Room.inMemoryDatabaseBuilder(context,WeatherRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        mWeatherDao=mWeatherRoomDatabase.weatherDao();
    }

    @After
    public void closeDB(){
        mWeatherRoomDatabase.close();
    }

    /**
     * expects insert a value to database
     * @throws InterruptedException
     */
    @Test
    public void insertWeatherTest() throws InterruptedException {
        WeatherModel mStockholm=new WeatherModel();
        mStockholm.setCityName("Stockholm");
        mWeatherDao.insertWeather(mStockholm);

        List<WeatherModel> actualValues =LiveDataTestUtil.getValue(mWeatherDao.getAllWeathersLiveData());
        assertNotNull(actualValues);
        int expectedSize=1;
        assertEquals(expectedSize,actualValues.size());
        assertEquals(actualValues.get(0).getCityName(),mStockholm.getCityName());
    }

    /**
     * Expects the database will be ignore duplicate primary key
     * @throws InterruptedException
     */
    @Test
    public void insertWeatherConflictIgnoreTest() throws InterruptedException {
        final int EXPECTED_SIZE=1;
        WeatherModel mWeather=new WeatherModel();
        mWeather.setCityName("Stockholm");
        mWeatherDao.insertWeather(mWeather);

        WeatherModel mWeather2=new WeatherModel();
        mWeather2.setCityName("Stockholm");
        mWeatherDao.insertWeather(mWeather2);

        List<WeatherModel> actualValue =LiveDataTestUtil.getValue(mWeatherDao.getAllWeathersLiveData());
        assertEquals(EXPECTED_SIZE,actualValue.size());
    }

    /**
     * expects to receive all data from database by LiveData solution.
     * @throws InterruptedException
     */
    @Test
    public void getAllWeathersLiveDataTest() throws InterruptedException {
        final int EXPECTED_SIZE=2;
        WeatherModel mWeather1=new WeatherModel();
        mWeather1.setCityName("Stockholm");
        mWeatherDao.insertWeather(mWeather1);
        WeatherModel mWeather2=new WeatherModel();
        mWeather2.setCityName("Tabriz");
        mWeatherDao.insertWeather(mWeather2);
        List<WeatherModel>actualValues =LiveDataTestUtil.getValue(mWeatherDao.getAllWeathersLiveData());
        assertEquals(EXPECTED_SIZE,actualValues.size());

    }

    /**
     * expects to receive all data from database.
     */
    @Test
    public void getAllWeathersTest()  {
        final int EXPECTED_SIZE=2;
        WeatherModel mWeather1=new WeatherModel();
        mWeather1.setCityName("Stockholm");
        mWeatherDao.insertWeather(mWeather1);
        WeatherModel mWeather2=new WeatherModel();
        mWeather2.setCityName("Tabriz");
        mWeatherDao.insertWeather(mWeather2);
        List<WeatherModel>actualValues =mWeatherDao.getAllWeathers();
        assertEquals(EXPECTED_SIZE,actualValues.size());

    }

    /**
     * expects to delete a specific row from database
     * @throws InterruptedException
     */
    @Test
    public void deleteWeatherTest() throws InterruptedException {
        WeatherModel mStockholm=new WeatherModel();
        mStockholm.setCityName("Stockholm");
        mWeatherDao.insertWeather(mStockholm);
        WeatherModel mWeather2=new WeatherModel();
        mWeather2.setCityName("Tabriz");
        mWeatherDao.insertWeather(mWeather2);
        mWeatherDao.deleteWeather(mStockholm);
        List<WeatherModel> actualValue =LiveDataTestUtil.getValue(mWeatherDao.getAllWeathersLiveData());
        assertTrue(!actualValue.contains(mStockholm));
    }

    /**
     * expects to get a specific row from database
     * @throws InterruptedException
     */
    @Test
    public void getWeatherTest() throws InterruptedException {
        final int EXPECTED_SIZE=2;
        WeatherModel mWeather1=new WeatherModel();
        mWeather1.setCityName("Stockholm");
        mWeatherDao.insertWeather(mWeather1);
        WeatherModel mWeather2=new WeatherModel();
        mWeather2.setCityName("Tabriz");
        mWeatherDao.insertWeather(mWeather2);
        List<WeatherModel>weathers =LiveDataTestUtil.getValue(mWeatherDao.getAllWeathersLiveData());
        assertEquals(EXPECTED_SIZE,weathers.size());
        WeatherModel actualValue =mWeatherDao.getWeather("Stockholm");
        assertNotNull(actualValue);
        assertEquals("Stockholm",actualValue.getCityName());
    }

    /**
     * expects to update all rows in the database.
     * @throws InterruptedException
     */
    @Test
    public void updateAllWeatherTest() throws InterruptedException {
        WeatherModel mStockholm=new WeatherModel();
        mStockholm.setCityName("Stockholm");
        mStockholm.setOrderValue(1);
        mWeatherDao.insertWeather(mStockholm);
        WeatherModel mSundsvall=new WeatherModel();
        mSundsvall.setCityName("Sundsvall");
        mSundsvall.setOrderValue(2);
        mWeatherDao.insertWeather(mSundsvall);
        //update the values
        mStockholm.setOrderValue(2);
        mSundsvall.setOrderValue(1);
        List<WeatherModel>weathers = new ArrayList<>();
        weathers.add(mStockholm);
        weathers.add(mSundsvall);
        mWeatherDao.updateAllWeathers(weathers);

        WeatherModel actualValue=mWeatherDao.getWeather("Stockholm");
        assertEquals(mStockholm.getOrderValue(),actualValue.getOrderValue());

        actualValue=mWeatherDao.getWeather("Sundsvall");
        assertEquals(mSundsvall.getOrderValue(),actualValue.getOrderValue());
    }

    /**
     * expects to get a default item from database
     */
    @Test
    public  void getDefaultItemTest(){
        WeatherModel mStockholm=new WeatherModel();
        mStockholm.setCityName("Stockholm");
        mStockholm.setOrderValue(1);
        mStockholm.setDefaultItem(false);
        mWeatherDao.insertWeather(mStockholm);
        WeatherModel mSundsvall=new WeatherModel();
        mSundsvall.setCityName("Sundsvall");
        mSundsvall.setOrderValue(2);
        mSundsvall.setDefaultItem(true);
        mWeatherDao.insertWeather(mSundsvall);
        WeatherModel actualValue=mWeatherDao.getDefaultItem();
        assertEquals("Sundsvall",actualValue.getCityName());
    }

    /**
     * expects to set a specific item as a default item in the database
     */
    @Test
    public  void setAsDefaultItemTest(){
        WeatherModel mStockholm=new WeatherModel();
        mStockholm.setCityName("Stockholm");
        mStockholm.setOrderValue(1);
        mWeatherDao.insertWeather(mStockholm);
        WeatherModel mSundsvall=new WeatherModel();
        mSundsvall.setCityName("Sundsvall");
        mSundsvall.setOrderValue(2);
        mWeatherDao.insertWeather(mSundsvall);
        mWeatherDao.setAsDefaultItem("Sundsvall");
        WeatherModel actualValue=mWeatherDao.getDefaultItem();
        assertEquals("Sundsvall",actualValue.getCityName());
    }

    /**
     * expects to reset all default Items to false.
     */
    @Test
    public  void resetAllDefaultItemValueTest(){
        WeatherModel mStockholm=new WeatherModel();
        mStockholm.setCityName("Stockholm");
        mStockholm.setOrderValue(1);
        mWeatherDao.insertWeather(mStockholm);
        WeatherModel mSundsvall=new WeatherModel();
        mSundsvall.setCityName("Sundsvall");
        mSundsvall.setOrderValue(2);
        mWeatherDao.insertWeather(mSundsvall);
        mWeatherDao.setAsDefaultItem("Sundsvall");
        mWeatherDao.resetAllDefaultItemValue();
        WeatherModel actualValue=mWeatherDao.getDefaultItem();
        assertNull(actualValue);
    }

    /**
     * expects to get database size +1
     */
    @Test
    public void getNewOrderNumberTest(){
        final int EXPECTED_VALUE=1;
        int actualValue=mWeatherDao.getNewOrderNumber();
        assertEquals(EXPECTED_VALUE,actualValue);
    }

}
