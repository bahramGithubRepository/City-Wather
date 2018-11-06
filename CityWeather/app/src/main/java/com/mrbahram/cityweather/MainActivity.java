package com.mrbahram.cityweather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrbahram.cityweather.Interface.FetchDataCallbackInterface;
import com.mrbahram.cityweather.Models.WeatherModel;
import com.mrbahram.cityweather.Repository.DatabaseHelper;

import com.mrbahram.cityweather.Repository.FetchDataFromWebAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements FetchDataCallbackInterface {
    // hash table for finding image id based on the image name.
    public static HashMap<String,Integer>imageCollection=new HashMap<>();
    SwipeRefreshLayout swipeRefreshLayout;
    public static WeatherModel SelectedCity=null;
    ArrayList<WeatherModel> CityName;
    ArrayAdapter<WeatherModel> mAdapter;
    ListView mListView;
    TextView mEmptyView;
    DatabaseHelper repository=new DatabaseHelper(this);
    WeatherModel firstCity=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(imageCollection.size()<=0){
            insertImageToImageCollection();
        }
        setContentView(R.layout.activity_main);

        Toolbar toolbar=(Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        mListView=(ListView)findViewById(R.id.list_main);
        mEmptyView=(TextView)findViewById(R.id.emptyView_main);
        mListView.setEmptyView(mEmptyView);
        CityName=new ArrayList<>();
        firstCity=repository.getFirst();
        if(firstCity!=null)
            CityName.add(firstCity);
        mAdapter=new CustomAdaptorMainList(CityName,getApplicationContext());
        mListView.setAdapter(mAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(CityName!=null && !CityName.isEmpty()){
                    getCityWeather(CityName.get(0).getLat(),CityName.get(0).getLon());
                }else{
                    swipeRefreshLayout.setRefreshing(false);

                }

            }
        });



    }


    /**
     * reset or set data when the application come from child activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(SelectedCity!=null){
            CityName.clear();
            CityName.add(SelectedCity);
            mAdapter.notifyDataSetChanged();
        }else if(firstCity==null){
            CityName.clear();
            mAdapter.notifyDataSetChanged();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //call AddLocationActivity when the location icon touched.
            case R.id.action_add_location:

                startActivity(new Intent(this, AddLocationActivity.class));
                return true;
            // Check if user triggered a refresh:
            case R.id.menu_refresh:


                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    //==========Helper Function============

    /**
     * call when re-load data has been requested
     * @param lat
     * @param lon
     */
    private void getCityWeather(double lat,double lon){

        if(isInternetConnection()) {
            String url = "http://api.apixu.com/v1/current.json?key=2c02c64e6f33476c901180818183110&" +
                    "q=" + Double.toString(lat) + "," + Double.toString(lon);
            new FetchDataFromWebAPI(this, ResultFor.Weather).execute(url);
        }else{
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(MainActivity.this, " No Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * pass fetched data from API for updating the ListView
     * @param result, fetched data
     * @param resultFor specify result for ResultFor.Search ot ResultFor.Weather
     */
    @Override
    public void fetchDataCallback(String result, ResultFor resultFor) {
        if(resultFor==ResultFor.Weather){
            createWeatherModel(result);
        }
    }

    /**
     * Pars a JSON result and update the ListView
     * @param result is a JSON object related for a weather of the city
     */
    private void createWeatherModel(String result){
        try {
            WeatherModel weatherModel=new WeatherModel();
            JSONObject jsonObject=new JSONObject(result);
            if(jsonObject.length()!=0){
                JSONObject location=jsonObject.getJSONObject("location");
                weatherModel.setCityName(location.getString("name"));
                weatherModel.setRegion(location.getString("region"));
                weatherModel.setCountry(location.getString("country"));
                weatherModel.setLat(location.getDouble("lat"));
                weatherModel.setLon(location.getDouble("lon"));
                weatherModel.setLocalTime(location.getString("localtime"));
                JSONObject current=jsonObject.getJSONObject("current");
                weatherModel.setTemperature_C(current.getDouble("temp_c"));
                JSONObject condition=current.getJSONObject("condition");
                weatherModel.setWeatherCondition(condition.getString("text"));
                String icon=condition.getString("icon").split("cdn.apixu.com/",2)[1];
                weatherModel.setIcon(icon);
                weatherModel.setFeelsLike_c(current.getDouble("feelslike_c"));

                repository.update(weatherModel);
                CityName.clear();
                CityName.add(weatherModel);
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

                //Toast.makeText(MainActivity.this, weatherModel.getCityName()+" updated", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {
            Log.d("My code", "fetchDataCallback -1: result is not JSON Array "+e.getMessage());
        }
    }

    /**
     * Check the internet connection
     * @return true if there is internet connection else return false
     */
    private boolean isInternetConnection(){

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    /**
     * Initiate imageCollection hash map
     */
    private void insertImageToImageCollection() {
        imageCollection.put("weather/64x64/night/113.png",R.mipmap.n113);
        imageCollection.put("weather/64x64/night/116.png",R.mipmap.n116);
        imageCollection.put("weather/64x64/night/119.png",R.mipmap.n119);
        imageCollection.put("weather/64x64/night/122.png",R.mipmap.n122);
        imageCollection.put("weather/64x64/night/143.png",R.mipmap.n143);
        imageCollection.put("weather/64x64/night/176.png",R.mipmap.n176);
        imageCollection.put("weather/64x64/night/179.png",R.mipmap.n179);
        imageCollection.put("weather/64x64/night/182.png",R.mipmap.n182);
        imageCollection.put("weather/64x64/night/185.png",R.mipmap.n185);
        imageCollection.put("weather/64x64/night/200.png",R.mipmap.n200);
        imageCollection.put("weather/64x64/night/227.png",R.mipmap.n227);
        imageCollection.put("weather/64x64/night/230.png",R.mipmap.n230);
        imageCollection.put("weather/64x64/night/248.png",R.mipmap.n248);
        imageCollection.put("weather/64x64/night/260.png",R.mipmap.n260);
        imageCollection.put("weather/64x64/night/263.png",R.mipmap.n263);
        imageCollection.put("weather/64x64/night/266.png",R.mipmap.n266);
        imageCollection.put("weather/64x64/night/281.png",R.mipmap.n281);
        imageCollection.put("weather/64x64/night/284.png",R.mipmap.n284);
        imageCollection.put("weather/64x64/night/293.png",R.mipmap.n293);
        imageCollection.put("weather/64x64/night/296.png",R.mipmap.n296);
        imageCollection.put("weather/64x64/night/299.png",R.mipmap.n299);
        imageCollection.put("weather/64x64/night/302.png",R.mipmap.n302);
        imageCollection.put("weather/64x64/night/305.png",R.mipmap.n305);
        imageCollection.put("weather/64x64/night/308.png",R.mipmap.n308);
        imageCollection.put("weather/64x64/night/311.png",R.mipmap.n311);
        imageCollection.put("weather/64x64/night/314.png",R.mipmap.n314);
        imageCollection.put("weather/64x64/night/317.png",R.mipmap.n317);
        imageCollection.put("weather/64x64/night/320.png",R.mipmap.n320);
        imageCollection.put("weather/64x64/night/323.png",R.mipmap.n323);
        imageCollection.put("weather/64x64/night/326.png",R.mipmap.n326);
        imageCollection.put("weather/64x64/night/329.png",R.mipmap.n329);
        imageCollection.put("weather/64x64/night/332.png",R.mipmap.n332);
        imageCollection.put("weather/64x64/night/335.png",R.mipmap.n335);
        imageCollection.put("weather/64x64/night/338.png",R.mipmap.n338);
        imageCollection.put("weather/64x64/night/350.png",R.mipmap.n350);
        imageCollection.put("weather/64x64/night/353.png",R.mipmap.n353);
        imageCollection.put("weather/64x64/night/356.png",R.mipmap.n356);
        imageCollection.put("weather/64x64/night/359.png",R.mipmap.n359);
        imageCollection.put("weather/64x64/night/362.png",R.mipmap.n362);
        imageCollection.put("weather/64x64/night/365.png",R.mipmap.n365);
        imageCollection.put("weather/64x64/night/368.png",R.mipmap.n368);
        imageCollection.put("weather/64x64/night/371.png",R.mipmap.n371);
        imageCollection.put("weather/64x64/night/374.png",R.mipmap.n374);
        imageCollection.put("weather/64x64/night/377.png",R.mipmap.n377);
        imageCollection.put("weather/64x64/night/386.png",R.mipmap.n386);
        imageCollection.put("weather/64x64/night/389.png",R.mipmap.n389);
        imageCollection.put("weather/64x64/night/392.png",R.mipmap.n392);
        imageCollection.put("weather/64x64/night/395.png",R.mipmap.n395);

        imageCollection.put("weather/64x64/day/113.png",R.mipmap.d113);
        imageCollection.put("weather/64x64/day/116.png",R.mipmap.d116);
        imageCollection.put("weather/64x64/day/119.png",R.mipmap.d119);
        imageCollection.put("weather/64x64/day/122.png",R.mipmap.d122);
        imageCollection.put("weather/64x64/day/143.png",R.mipmap.d143);
        imageCollection.put("weather/64x64/day/176.png",R.mipmap.d176);
        imageCollection.put("weather/64x64/day/179.png",R.mipmap.d179);
        imageCollection.put("weather/64x64/day/182.png",R.mipmap.d182);
        imageCollection.put("weather/64x64/day/185.png",R.mipmap.d185);
        imageCollection.put("weather/64x64/day/200.png",R.mipmap.d200);
        imageCollection.put("weather/64x64/day/227.png",R.mipmap.d227);
        imageCollection.put("weather/64x64/day/230.png",R.mipmap.d230);
        imageCollection.put("weather/64x64/day/248.png",R.mipmap.d248);
        imageCollection.put("weather/64x64/day/260.png",R.mipmap.d260);
        imageCollection.put("weather/64x64/day/263.png",R.mipmap.d263);
        imageCollection.put("weather/64x64/day/266.png",R.mipmap.d266);
        imageCollection.put("weather/64x64/day/281.png",R.mipmap.d281);
        imageCollection.put("weather/64x64/day/284.png",R.mipmap.d284);
        imageCollection.put("weather/64x64/day/293.png",R.mipmap.d293);
        imageCollection.put("weather/64x64/day/296.png",R.mipmap.d296);
        imageCollection.put("weather/64x64/day/299.png",R.mipmap.d299);
        imageCollection.put("weather/64x64/day/302.png",R.mipmap.d302);
        imageCollection.put("weather/64x64/day/305.png",R.mipmap.d305);
        imageCollection.put("weather/64x64/day/308.png",R.mipmap.d308);
        imageCollection.put("weather/64x64/day/311.png",R.mipmap.d311);
        imageCollection.put("weather/64x64/day/314.png",R.mipmap.d314);
        imageCollection.put("weather/64x64/day/317.png",R.mipmap.d317);
        imageCollection.put("weather/64x64/day/320.png",R.mipmap.d320);
        imageCollection.put("weather/64x64/day/323.png",R.mipmap.d323);
        imageCollection.put("weather/64x64/day/326.png",R.mipmap.d326);
        imageCollection.put("weather/64x64/day/329.png",R.mipmap.d329);
        imageCollection.put("weather/64x64/day/332.png",R.mipmap.d332);
        imageCollection.put("weather/64x64/day/335.png",R.mipmap.d335);
        imageCollection.put("weather/64x64/day/338.png",R.mipmap.d338);
        imageCollection.put("weather/64x64/day/350.png",R.mipmap.d350);
        imageCollection.put("weather/64x64/day/353.png",R.mipmap.d353);
        imageCollection.put("weather/64x64/day/356.png",R.mipmap.d356);
        imageCollection.put("weather/64x64/day/359.png",R.mipmap.d359);
        imageCollection.put("weather/64x64/day/362.png",R.mipmap.d362);
        imageCollection.put("weather/64x64/day/365.png",R.mipmap.d365);
        imageCollection.put("weather/64x64/day/368.png",R.mipmap.d368);
        imageCollection.put("weather/64x64/day/371.png",R.mipmap.d371);
        imageCollection.put("weather/64x64/day/374.png",R.mipmap.d374);
        imageCollection.put("weather/64x64/day/377.png",R.mipmap.d377);
        imageCollection.put("weather/64x64/day/386.png",R.mipmap.d386);
        imageCollection.put("weather/64x64/day/389.png",R.mipmap.d389);
        imageCollection.put("weather/64x64/day/392.png",R.mipmap.d392);
        imageCollection.put("weather/64x64/day/395.png",R.mipmap.d395);

    }
}
