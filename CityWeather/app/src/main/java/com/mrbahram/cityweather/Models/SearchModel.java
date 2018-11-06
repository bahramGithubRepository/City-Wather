package com.mrbahram.cityweather.Models;

/**
 * Search model
 * It can be used for saving search result of WEB API
 */
public class SearchModel {
    private String CityName;
    private double lat;
    private double lon;

    public SearchModel(String cityName, double lat, double lon) {
        CityName = cityName;
        this.lat = lat;
        this.lon = lon;
    }

    public String getCityName() {
        return CityName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return "SearchModel{" +
                "CityName='" + CityName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
