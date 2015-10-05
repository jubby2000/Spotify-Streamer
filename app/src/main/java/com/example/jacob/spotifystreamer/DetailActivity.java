package com.example.jacob.spotifystreamer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by jacob on 6/29/15.
 */
public class DetailActivity extends ActionBarActivity {

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Intent getParentActivityIntent() {
        // add the clear top flag - which checks if the parent (main)
        // activity is already running and avoids recreating it
        return super.getParentActivityIntent()
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artist");

        getSupportActionBar().setSubtitle(artistName);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/

    public static class DetailFragment extends Fragment {

        public static final String LOG_TAG = DetailFragment.class.getSimpleName();

        //ListOfTracks[] trackViews = {};

        boolean mIsLargeLayout;
        private SongArrayAdapter mSongArrayAdapter;

        private ArrayList<ListOfTracks> trackList;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mIsLargeLayout = getResources().getBoolean(R.bool.large_layout);

            if (savedInstanceState == null || !savedInstanceState.containsKey("tracks")) {
                trackList = new ArrayList<ListOfTracks>();
            } else {
                trackList = savedInstanceState.getParcelableArrayList("tracks");
            }

        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            if (trackList != null) {
                outState.putParcelableArrayList("tracks", trackList);
            }
            super.onSaveInstanceState(outState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            View rootView = inflater.inflate(R.layout.track_list_layout, container, false);

            String artistId = "";
            //String artistName = "";


            Intent intent = getActivity().getIntent();

            if (intent.getStringExtra("artistId") != null) {
                artistId = intent.getStringExtra("artistId");
            }
            if (getArguments() != null) {
                artistId = getArguments().getString("artistId");
            }

            mSongArrayAdapter = new SongArrayAdapter(getActivity(), trackList);//new ArrayList<ListOfTracks>());

            final ListView listView = (ListView) rootView.findViewById(R.id.track_list);
            listView.setAdapter(mSongArrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                    String artistName;

                    int currentPosition = i;
                    if (getArguments() != null) {
                        artistName = getArguments().getString("artist");
                    } else {
                        artistName = getActivity().getIntent().getStringExtra("artistName");
                    }

                    //String artistName = mSongArrayAdapter.getItem(i).artistName;
                    String albumName = mSongArrayAdapter.getItem(i).albumName;
                    String albumArtLarge = mSongArrayAdapter.getItem(i).largeImage;
                    String trackName = mSongArrayAdapter.getItem(i).trackName;
                    String previewUrl = mSongArrayAdapter.getItem(i).previewUrl;
                    String artistImageLarge = getActivity().getIntent().getStringExtra("artistImageLarge");

                    if (!mIsLargeLayout) {
                        Intent intent = new Intent(getActivity(), PlayerActivity.class)
                                .putExtra("artistName", artistName)
                                .putExtra("albumName", albumName)
                                .putExtra("albumArtLarge", albumArtLarge)
                                .putExtra("trackName", trackName)
                                .putExtra("previewUrl", previewUrl)
                                .putParcelableArrayListExtra("trackList", trackList)
                                .putExtra("currentPosition", currentPosition)
                                .putExtra("artistImageLarge", artistImageLarge);

                        startActivity(intent);
                    } else {
                        PlayerDialogFragment playerFragment = new PlayerDialogFragment();

                        Bundle args = new Bundle();
                        args.putString("artistName", artistName);
                        args.putString("albumName", albumName);
                        args.putString("albumArtLarge", albumArtLarge);
                        args.putString("trackName", trackName);
                        args.putString("previewUrl", previewUrl);
                        args.putParcelableArrayList("trackList", trackList);
                        args.putInt("currentPosition", currentPosition);
                        args.putString("artistImageLarge", artistImageLarge);

                        playerFragment.setArguments(args);

                        FragmentManager fragmentManager = getFragmentManager();

                        playerFragment.show(fragmentManager, "player");

                    }



//                    DialogFragment newFragment = PlayerDialogFragment.newInstance(artistName,
//                            albumName, albumArtLarge, trackName, previewUrl);

//                    FragmentManager fragmentManager = getFragmentManager();

//                    if (mIsLargeLayout) {
//                        // The device is using a large layout, so show the fragment as a dialog
//                        newFragment.show(fragmentManager, "dialog");
//                    } else {
//                        // The device is smaller, so show the fragment fullscreen
//                        FragmentTransaction transaction = fragmentManager.beginTransaction();
//                        // For a little polish, specify a transition animation
//                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                        // To make it fullscreen, use the 'content' root view as the container
//                        // for the fragment, which is always the root view for the activity
//                        transaction.add(android.R.id.content, newFragment)
//                                .addToBackStack(null).commit();
//                    }
//
//                    newFragment.show(getFragmentManager(), "dialog");
//
//                    playerFragment.setArguments(args);
//
//                    getFragmentManager().beginTransaction()
//                            .replace(R.id.player_fragment, playerFragment, "dialog")
//                            .addToBackStack(null)
//                            .commit();
//
//                    playerFragment.show(getFragmentManager(), "dialog");
                }
            });


            //String country = intent.getStringExtra("country"); //Probably unnecessary

            FetchTopSongsTask topSongsTask = new FetchTopSongsTask();

            if (savedInstanceState == null) {
                topSongsTask.execute(artistId);
            }

            return rootView;
        }


        public class FetchTopSongsTask extends AsyncTask<String, Void, List<Track>> {

            private final String LOG_TAG = FetchTopSongsTask.class.getSimpleName();

            public List<Track> getArtistTopTrack(@Path("id") String artistId, @QueryMap Map<String, Object> options) {
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                Map<String, Object> map = new HashMap<>();
                map.put("country", "US");

                try {
                    Tracks results = spotify.getArtistTopTrack(artistId, map);
                    List<Track> topTracks = results.tracks;
                    if (topTracks.size() == 0){
                        Snackbar.make(getActivity().findViewById(R.id.track_list),
                                "I can't find any tracks for that artist.",
                                Snackbar.LENGTH_LONG).show();
                        return null;
                    }
                    return topTracks;
                } catch (RetrofitError error) {
                    Snackbar.make(getActivity().findViewById(R.id.track_list),
                            "YO DAWG YOU NEED INTERNET TO INTERNET",
                            Snackbar.LENGTH_LONG).show();
                    return null;
                }

            }

            @Override
            protected List<Track> doInBackground(String... query) {

                if (query.length == 0) {
                    return null;
                }

                List<Track> tracks = getArtistTopTrack(query[0], null);

                return tracks;
            }

            @Override
            protected void onPostExecute(List<Track> topTracks) {
                super.onPostExecute(topTracks);

                if (topTracks == null) {
                    mSongArrayAdapter.clear();
                } else {

                    ArrayList<ListOfTracks> trackStore = new ArrayList<ListOfTracks>();

                    int rightSize = 0;
                    if (topTracks.size() > 0) {
                        for (int i = 0; i < topTracks.size(); i++) {
                            if (!topTracks.get(i).album.images.isEmpty()) {
                                for (int i1 = 0; i1 < topTracks.get(i).album.images.size(); i1++) {
                                    if (topTracks.get(i).album.images.get(i1).height < 200 &&
                                            topTracks.get(i).album.images.get(i1).width < 200) {
                                        rightSize = i1 - 1;
                                    }
                                }

                                String largeImage = topTracks.get(i).album.images.get(0).url;
                                String image = topTracks.get(i).album.images.get(rightSize).url;
                                String trackName = topTracks.get(i).name;
                                String albumName = topTracks.get(i).album.name;
                                String previewUrl = topTracks.get(i).preview_url;
                                String artistName = getActivity().getIntent().getStringExtra("artist");
                                trackStore.add(new ListOfTracks(largeImage, image, trackName,
                                        albumName, previewUrl, artistName));
                                mSongArrayAdapter.add(new ListOfTracks(largeImage, image, trackName,
                                        albumName, previewUrl, artistName));
                            } else {
                                String largeImage = "http://lorempixel.com/640/640/cats/";
                                String image = "http://lorempixel.com/200/200/cats/";
                                String trackName = topTracks.get(i).name;
                                String albumName = topTracks.get(i).album.name;
                                String previewUrl = topTracks.get(i).preview_url;
                                String artistName;
                                if (getArguments() != null) {
                                    artistName = getArguments().getString("artistName");
                                } else {
                                    artistName = getActivity().getIntent().getStringExtra("artist");
                                }
                                trackStore.add(new ListOfTracks(largeImage, image, trackName,
                                        albumName, previewUrl, artistName));
                                mSongArrayAdapter.add(new ListOfTracks(largeImage, image, trackName,
                                        albumName, previewUrl, artistName));
                            }
                        }
                    }
                    trackList = trackStore;
                }
            }
        }
    }
}