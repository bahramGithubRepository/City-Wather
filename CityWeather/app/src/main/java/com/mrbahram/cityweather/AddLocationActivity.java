package com.mrbahram.cityweather;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.mrbahram.cityweather.Models.WeatherModel;
import com.mrbahram.cityweather.ViewModels.WeatherViewModel;
import java.util.ArrayList;
import java.util.List;


/**
 * Add location activity
 */
public class AddLocationActivity extends AppCompatActivity {
    public static final int SEARCH_LOCATION_ACTIVITY_REQUEST_CODE = 1;

    CustomAdapterAddLocationActivityList mAdapter;
    TouchListView mListView;
    TextView mEmptyView;

    private WeatherViewModel mWeatherViewModel;
    public static final String EXTRA_REPLY = "REPLY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        //Initialize Toolbar
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.add_location_toolbar);
        setSupportActionBar(myChildToolbar);

        myChildToolbar.setTitleTextColor(Color.WHITE);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        //Initialize FAB button
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSearch();
            }
        });
        //Initialize the View Model
        mWeatherViewModel=ViewModelProviders.of(this).get(WeatherViewModel.class);
        //Initialize List View
        mListView=(TouchListView)findViewById(R.id.list_location);
        //Initialize empty view of the list view
        mEmptyView=(TextView)findViewById(R.id.emptyViewLocation);
        mListView.setEmptyView(mEmptyView);

        //Get List of Available weathers information from database
        List<WeatherModel> weathers=mWeatherViewModel.getAllWeathers();

        /**
         * Initialize the custom adaptor of the listView
         */
        mAdapter=new CustomAdapterAddLocationActivityList(getApplicationContext(),weathers);
        mListView.setAdapter(mAdapter);

        //Notify and update the UI when the data has been changed
        mWeatherViewModel.getAllWeathersLiveData().observe(this, new Observer<List<WeatherModel>>() {
            @Override
            public void onChanged(@Nullable List<WeatherModel> weatherModels) {

                mAdapter.clear();
                mAdapter.addAll(weatherModels);
                mAdapter.notifyDataSetChanged();
            }
        });
        //Initiate Drop listener for the ListView
        mListView.setDropListener(onDrop);

        // Set onclick listener for each element of the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position,
                                    long arg3) {
                WeatherModel selectedItem=(WeatherModel) adapterView.getItemAtPosition(position);
                mWeatherViewModel.resetAllDefaultItem();
                mWeatherViewModel.setAsDefaultItem(selectedItem.getCityName());

                // Create an Intent to go to parent activity and pass city name to parent activity for further request.
                Intent upIntent = NavUtils.getParentActivityIntent(AddLocationActivity.this);
                upIntent.putExtra(EXTRA_REPLY, selectedItem.getCityName());
                setResult(RESULT_OK, upIntent);

                finish();
            }
        });


        // Create an instance of SwipeDismissListViewTouchListener to remove
        // a row from The ListView
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        mListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }


                            @Override
                            public void onDismiss(ListView tlv, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    String city=mAdapter.getItem(position).getCityName();
                                    mWeatherViewModel.deleteWeather(mAdapter.getItem(position));
                                    // removes data from the adaptor
                                    mAdapter.remove(mAdapter.getItem(position));
                                    mAdapter.notifyDataSetChanged();

                                }

                            }
                        });

        mListView.setOnTouchListener(touchListener);


    }


    /**
     * Start Search activity
     */
    private void callSearch(){
        startActivityForResult(new Intent(this, SearchActivity.class),SEARCH_LOCATION_ACTIVITY_REQUEST_CODE);
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
     *Re-Order the ListView
     */
    private TouchListView.DropListener onDrop=new TouchListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            WeatherModel item=mAdapter.getItem(from);
            mAdapter.remove(item);
            mAdapter.insert(item, to);
            List<WeatherModel>newOrderedOfList=new ArrayList<>();
            for(int i=0;i<mAdapter.getCount();i++){
                WeatherModel weatherModel=mAdapter.getItem(i);
                weatherModel.setOrderValue(i+1);
                newOrderedOfList.add(weatherModel);

            }
            mWeatherViewModel.setNewOrder(newOrderedOfList);

        }
    };

}

