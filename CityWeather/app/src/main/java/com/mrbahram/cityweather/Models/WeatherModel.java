package com.mrbahram.cityweather.Models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Weather Model
 * It can be used for saving weather result of WEB API in the database
 */
//Table name
@Entity(tableName = "weather")
public class WeatherModel {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "cityName")
    private String cityName;
    @ColumnInfo(name = "region")
    private String region;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "lat")
    private double lat;
    @ColumnInfo(name = "lon")
    private double lon;
    @ColumnInfo(name = "localTime")
    private String localTime;
    @ColumnInfo(name = "temperature_C")
    private  double temperature_C;
    @ColumnInfo(name = "weatherCondition")
    private String weatherCondition;
    @ColumnInfo(name = "icon")
    private String icon;
    @ColumnInfo(name = "feelsLike_c")
    private double feelsLike_c;
    /**
     * This property saves order of object in a list. Minimum value is 1.
     */
    @ColumnInfo(name = "orderValue")
    private  int orderValue;
    /**
     * This property clarifies a object is a default.
     * The defaultItem must be true when the object has been selected in the AddLocationActivity list.
     */
    @ColumnInfo(name="defaultItem")
    private boolean defaultItem=false;


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

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public boolean isDefaultItem() {
        return defaultItem;
    }

    public void setDefaultItem(boolean defaultItem) {
        this.defaultItem = defaultItem;
    }
}
