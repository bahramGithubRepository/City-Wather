package com.mrbahram.citywather.Models;

/**
 * Weather Model
 * It can be used for saving weather result of WEB API
 */
public class WeatherModel {
    private String cityName;
    private String region;
    private String country;
    private double lat;
    private double lon;
    private String localTime;
    private  double temperature_C;
    private String weatherCondition;
    private String icon;
    private double feelsLike_c;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    public double getTemperature_C() {
        return temperature_C;
    }

    public void setTemperature_C(double temperature_C) {
        this.temperature_C = temperature_C;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public double getFeelsLike_c() {
        return feelsLike_c;
    }

    public void setFeelsLike_c(double feelsLike_c) {
        this.feelsLike_c = feelsLike_c;
    }

}
