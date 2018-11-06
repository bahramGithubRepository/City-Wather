package com.mrbahram.citywather;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrbahram.citywather.Interface.FetchDataCallbackInterface;
import com.mrbahram.citywather.Models.SearchModel;
import com.mrbahram.citywather.Models.WeatherModel;
import com.mrbahram.citywather.Repository.DatabaseHelper;

import com.mrbahram.citywather.Repository.FetchDataFromWebAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements FetchDataCallbackInterface {
    ArrayAdapter<String> mAdapter;
    ListView mListView;
    TextView mEmptyView;
    ArrayList<String> CityList;
    ArrayList<SearchModel> listOfCity;
    DatabaseHelper repository=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.search_toolbar);
        myChildToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myChildToolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        mListView=(ListView)findViewById(R.id.list_search);
        mEmptyView=(TextView)findViewById(R.id.emptyView);
        mListView.setEmptyView(mEmptyView);
        CityList=new ArrayList<>();

        mAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,CityList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String cityName=adapterView.getItemAtPosition(i).toString();

                for(SearchModel item:listOfCity){
                    if(item.getCityName().equals(cityName)){
                        double  lat=item.getLat();
                        double  lon=item.getLon();

                        getCityWeather(lat,lon);
                        break;
                    }
                }

            }
        });
    }

    /**
     * Call webAPI to get city's weather
     * @param lat
     * @param lon
     */
    private void getCityWeather(double lat,double lon){
        //SwipeRefreshLayout.setRefreshing(true);
        if(isInternetConnection()) {
            String url = "http://api.apixu.com/v1/current.json?key=2c02c64e6f33476c901180818183110&" +
                    "q=" + Double.toString(lat) + "," + Double.toString(lon);
            new FetchDataFromWebAPI(this, ResultFor.Weather).execute(url);
        }else{
            Toast.makeText(this, " No Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_search, menu);

        MenuItem mSearch = menu.findItem(R.id.app_bar_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.requestFocusFromTouch();
        mSearchView.setFocusable(true);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.requestFocus();
        mSearch.expandActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newCityName) {
                //call search function with new value
                getNewData(newCityName);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * call API for getting data based on the new input value.
     * @param newCityName is a new city name.
     */
    protected void getNewData(String newCityName) {
        if(isInternetConnection()){
            String url="http://api.apixu.com/v1/search.json?key=2c02c64e6f33476c901180818183110&q="+newCityName;

            new FetchDataFromWebAPI(this,ResultFor.Search).execute(url);

        }else{
            Toast.makeText(this, " No Internet Connection!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * pass fetched data from API for updating the ListView
     * @param result, fetched data
     * @param resultFor specify result for ResultFor.Search ot ResultFor.Weather
     */
    @Override
    public void fetchDataCallback(String result,ResultFor resultFor) {
        if(resultFor==ResultFor.Search)
            UpdateListView(result);
        else createWeatherModel(result);
    }
    /**
     * Pars a JSON result and update the ListView
     * @param result is a JSON object related for a search of the city name
     */
    protected void UpdateListView(String result){
        Log.d("My code", "fetchDataCallback 1 : "+result);
        try {
            listOfCity=new ArrayList<SearchModel>();

            JSONArray jsonArray=new JSONArray(result);

            for (int i=0;i<jsonArray.length();i++){
                String city=jsonArray.getJSONObject(i).getString("name");
                double lat=jsonArray.getJSONObject(i).getDouble("lat");
                double lon=jsonArray.getJSONObject(i).getDouble("lon");
                listOfCity.add(new SearchModel(city,lat,lon));
            }
            mAdapter.clear();

            for(SearchModel item:listOfCity){
                mAdapter.insert(item.getCityName(),mAdapter.getCount());
            }

            mAdapter.notifyDataSetChanged();


        } catch (JSONException e) {
            Log.d("My code", "fetchDataCallback 0: result is not JSON Array");
        }
    }
    /**
     * Pars a JSON result and save information into database
     * @param result is a JSON object related for a weather of the city
     */
    protected void createWeatherModel(String result){
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

                repository.add(weatherModel);

                //close the activity and come back to parent activity
                finish();
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
}
