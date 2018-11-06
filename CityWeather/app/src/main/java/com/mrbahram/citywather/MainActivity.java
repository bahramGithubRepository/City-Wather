package com.mrbahram.citywather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mrbahram.citywather.Models.WeatherModel;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static WeatherModel SelectedCity=null;
    public static HashMap<String,Integer> imageCollection=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
