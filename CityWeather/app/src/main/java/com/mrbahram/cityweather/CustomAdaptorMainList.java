package com.mrbahram.cityweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrbahram.cityweather.Models.WeatherModel;

import java.util.List;


/**
 * This is a custom adaptor for the ListView of the MainActivity
 */
public class CustomAdaptorMainList extends ArrayAdapter<WeatherModel> {

    private List<WeatherModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCityName;
        TextView txtCountry;
        TextView txtDate;
        TextView txtTemp;
        TextView txtWeather;
        TextView txtFeels;
        ImageView imgWeather;
    }

    public CustomAdaptorMainList(List<WeatherModel> data, Context context) {
        super(context, R.layout.adaptor_main_list
                , data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WeatherModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        CustomAdaptorMainList.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new CustomAdaptorMainList.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adaptor_main_list, parent, false);
            viewHolder.txtCityName = (TextView) convertView.findViewById(R.id.city_main);
            viewHolder.txtCountry = (TextView) convertView.findViewById(R.id.country_main);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.date_main);
            viewHolder.txtTemp = (TextView) convertView.findViewById(R.id.temp_main);
            viewHolder.txtWeather= (TextView) convertView.findViewById(R.id.weather_main);
            viewHolder.txtFeels = (TextView) convertView.findViewById(R.id.feels_main);
            viewHolder.imgWeather = (ImageView) convertView.findViewById(R.id.img_weather_main);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CustomAdaptorMainList.ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtCityName.setText(dataModel.getCityName());
        viewHolder.txtCountry.setText(dataModel.getRegion()+", "+dataModel.getCountry());
        viewHolder.txtDate.setText("Updated "+dataModel.getLocalTime());
        viewHolder.txtTemp.setText(dataModel.getTemperature_C()+"");
        viewHolder.txtWeather.setText(dataModel.getWeatherCondition());
        viewHolder.txtFeels.setText("Feels Like "+dataModel.getFeelsLike_c());
        viewHolder.imgWeather.setImageResource(MainActivity.imageCollection.get(dataModel.getIcon()));


        // Return the completed view to render on screen
        return convertView;

    }


}



