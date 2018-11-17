package com.mrbahram.cityweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mrbahram.cityweather.Models.WeatherModel;


import java.util.Collections;
import java.util.List;

/**
 * This is a custom adaptor for the ListView of the AddLocationActivity
 */
public class CustomAdapterAddLocationActivityList extends ArrayAdapter<WeatherModel>{

    private List<WeatherModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtCityName;
        TextView txtCountry;
        TextView txtDate;
        TextView txtTemp;
        TextView txtFeels;
        ImageView imgWeather;
    }

    //public CustomAdapterAddLocationActivityList(List<WeatherModel> data, Context context) {
    public CustomAdapterAddLocationActivityList( Context context,List<WeatherModel> data) {
        super(context, R.layout.adapter_layout_location_list,data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        WeatherModel dataModel =dataSet.get(position);// getItem(position);
        Log.d("new order", "new order "+dataModel.getCityName());
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_layout_location_list, parent, false);
            viewHolder.txtCityName = (TextView) convertView.findViewById(R.id.city_name);
            viewHolder.txtCountry = (TextView) convertView.findViewById(R.id.country);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.date);
            viewHolder.txtTemp = (TextView) convertView.findViewById(R.id.temp);
            viewHolder.txtFeels = (TextView) convertView.findViewById(R.id.feels);
            viewHolder.imgWeather = (ImageView) convertView.findViewById(R.id.img_weather);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        if(dataModel.isDefaultItem())
            viewHolder.txtCityName.setText(dataModel.getCityName()+"  (Default)");
        else
            viewHolder.txtCityName.setText(dataModel.getCityName());

        viewHolder.txtCityName.setTextColor(mContext.getResources().getColor(R.color.PrimaryText) );
        viewHolder.txtCountry.setText(dataModel.getRegion()+", "+dataModel.getCountry());
        viewHolder.txtCountry.setTextColor(mContext.getResources().getColor(R.color.SecondaryText) );
        viewHolder.txtDate.setText("Updated "+dataModel.getLocalTime());
        viewHolder.txtDate.setTextColor(mContext.getResources().getColor(R.color.SecondaryText) );
        viewHolder.txtTemp.setText(dataModel.getTemperature_C()+"");
        viewHolder.txtFeels.setText(dataModel.getWeatherCondition());
        viewHolder.imgWeather.setImageResource(MainActivity.imageCollection.get(dataModel.getIcon()));


        // Return the completed view to render on screen
        return convertView;

    }
    void setWeather(List<WeatherModel> weather){
        dataSet = weather;
        notifyDataSetChanged();
    }
}

