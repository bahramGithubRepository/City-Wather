package com.mrbahram.cityweather.Repository;

import android.os.AsyncTask;

import com.mrbahram.cityweather.Interface.FetchDataCallbackInterface;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Fetch weather data from www.apixu.com API
 */
public class FetchDataFromWebAPI extends AsyncTask<String,Void,String> {
    /**
     * the callbackInterface value will be used for returning
     * fetched data to parent activity
     */
    FetchDataCallbackInterface callbackInterface;
    HttpURLConnection urlConnection;
    /**
     * There is two king of output/input parameters.
     * 1- ResultFor.Search: It is a output/input for searching a city name.
     * 2- ResultFor.Weather:It is output/input for searching  weather for specific city.
     */
    FetchDataCallbackInterface.ResultFor resultFor;
    private final int API_URL =0;

    public FetchDataFromWebAPI(FetchDataCallbackInterface callbackInterface,FetchDataCallbackInterface.ResultFor resultFor) {
        this.callbackInterface = callbackInterface;
        this.resultFor=resultFor;
    }

    /**
     * Background thread for getting data from www.apixu.com API
     * @param strings, position 0 is saved API url
     * @return fetched data from API
     */
    @Override
    protected String doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(strings[API_URL]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }

    /**
     * Send data to parent activity or class
     * @param fetchedData fetched data from API
     */
    @Override
    protected void onPostExecute(String fetchedData) {
        callbackInterface.fetchDataCallback(fetchedData,resultFor);
    }
}
