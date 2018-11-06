package com.mrbahram.citywather.Interface;

public interface FetchDataCallbackInterface {
    /**
     * There is two king of output/input parameters.
     * 1- ResultFor.Search: It is a output/input for searching a city name.
     * 2- ResultFor.Weather:It is output/input for searching  weather for specific city.
     */
    public enum ResultFor{Search,Weather}
    // method called when server's data get fetched
    public void fetchDataCallback (String result,ResultFor resultFor);
}
