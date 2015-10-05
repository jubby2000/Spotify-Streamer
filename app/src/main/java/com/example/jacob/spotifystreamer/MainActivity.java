package com.example.jacob.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements ArtistFragment.OnPassBack{

    //private final String LOG_TAG = MainActivity.class.getSimpleName();

    boolean mIsLargeLayout;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

        if (mIsLargeLayout) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.top_tracks_container, new DetailActivity.DetailFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        }

//        ArtistFragment artistFragment =  ((ArtistFragment)getSupportFragmentManager()
//                .findFragmentById(R.id.fragment_artists));
//        forecastFragment.setUseTodayLayout(!mTwoPane);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        int id = item.getItemId();
/*
        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }
*/
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPassBack(String artistName, String artistId, String country, String artistImageLarge) {
        Log.v("LOG", artistName + " " + artistId + " " + country + " " + artistImageLarge);

        if (mIsLargeLayout) {
            Bundle args = new Bundle();
            args.putString("artist", artistName);
            args.putString("artistId", artistId);
            args.putString("country", country);
            args.putString("artistImageLarge", artistImageLarge);

            DetailActivity.DetailFragment fragment = new DetailActivity.DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_tracks_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra("artist", artistName)
                    .putExtra("artistId", artistId)
                    .putExtra("country", country)
                    .putExtra("artistImageLarge", artistImageLarge);
            startActivity(intent);
        }
        }
    }

//    @Override
//    public void onItemSelected(String artistName, String artistId, String country, String artistImageLarge) {
//        if (mIsLargeLayout) {
//            // In two-pane mode, show the detail view in this activity by
//            // adding or replacing the detail fragment using a
//            // fragment transaction.

//    }


