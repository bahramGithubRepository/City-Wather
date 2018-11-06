package com.mrbahram.citywather;
import android.content.Intent;
import android.graphics.Color;
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
import com.mrbahram.citywather.Models.WeatherModel;
import com.mrbahram.citywather.Repository.DatabaseHelper;
import java.util.ArrayList;

/**
 * Add location activity
 */
public class AddLocationActivity extends AppCompatActivity {

    CustomAdapterAddLocationActivityList mAdapter;
    TouchListView mListView;
    TextView mEmptyView;
    ArrayList<WeatherModel>CityName;
    DatabaseHelper repository=new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        // my_child_toolbar is defined in the layout file
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.add_location_toolbar);
        setSupportActionBar(myChildToolbar);

        myChildToolbar.setTitleTextColor(Color.WHITE);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callSearch();
            }
        });
        // get all data from database
        ArrayList<WeatherModel>data=repository.getAll();

        CityName=data;

        mListView=(TouchListView)findViewById(R.id.list_location);

        mEmptyView=(TextView)findViewById(R.id.emptyViewLocation);
        mListView.setEmptyView(mEmptyView);

        /**
         * initiate the custom adaptor the listView
         */
        mAdapter=new CustomAdapterAddLocationActivityList(CityName,getApplicationContext());
        mListView.setAdapter(mAdapter);

        //Initiate Drop listener for the ListView
        mListView.setDropListener(onDrop);

        // Set onclick listener for each element of the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position,
                                    long arg3) {
                WeatherModel selectedItem=(WeatherModel) adapterView.getItemAtPosition(position);
                //Toast.makeText(AddLocationActivity.this, selectedItem.getCityName(), Toast.LENGTH_SHORT).show();
                // Create an Intent to go to parent activity
                Intent upIntent = NavUtils.getParentActivityIntent(AddLocationActivity.this);
                // pass selected item information to parent activity
                MainActivity.SelectedCity=selectedItem;
                NavUtils.navigateUpTo(AddLocationActivity.this, upIntent);
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
                                    String city=CityName.get(position).getCityName();
                                    repository.remove(city);
                                    CityName.remove(position);
                                    mAdapter.notifyDataSetChanged();
                                    //reset parent value if it was deleted.
                                    if(MainActivity.SelectedCity!=null &&MainActivity.SelectedCity.getCityName().equals(city))
                                        MainActivity.SelectedCity=null;


                                }

                            }
                        });

        mListView.setOnTouchListener(touchListener);


    }

    /**
     * Update the ListView with new data
     */
    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<WeatherModel>data=repository.getAll();
        CityName=data;

        mAdapter=new CustomAdapterAddLocationActivityList(CityName,getApplicationContext());;

        mListView.setAdapter(mAdapter);


    }

    /**
     * Start Search activity
     */
    private void callSearch(){
        startActivity(new Intent(this, SearchActivity.class));
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
            ArrayList<WeatherModel>newOrderedOfList=new ArrayList<>();
            for(int i=0;i<mAdapter.getCount();i++){
                newOrderedOfList.add(mAdapter.getItem(i));

            }
            repository.updateOrder(newOrderedOfList);

        }
    };

}

