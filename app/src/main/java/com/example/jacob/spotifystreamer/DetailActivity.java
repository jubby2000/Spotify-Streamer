package com.example.jacob.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by jacob on 6/29/15.
 */
public class DetailActivity extends ActionBarActivity {

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

        private SongArrayAdapter mSongArrayAdapter;

        private ArrayList<ListOfTracks> trackList;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

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

            mSongArrayAdapter = new SongArrayAdapter(getActivity(), trackList);//new ArrayList<ListOfTracks>());

            ListView listView = (ListView) rootView.findViewById(R.id.track_list);
            listView.setAdapter(mSongArrayAdapter);

            Intent intent = getActivity().getIntent();
            //String artistName = intent.getStringExtra("artist");
            String artistId = intent.getStringExtra("artistId");
            //String country = intent.getStringExtra("country"); //Probably unnecessary

            FetchTopSongsTask topSongsTask = new FetchTopSongsTask();

            if (savedInstanceState == null) {
                topSongsTask.execute(artistId);
            }

            return rootView;
        }



        public class FetchTopSongsTask extends AsyncTask<String, Void, List<Track>> {

            private final String LOG_TAG = FetchTopSongsTask.class.getSimpleName();

            public List<Track> getArtistTopTrack(@Path("id") String artistId, @QueryMap Map<String, Object> options){
                SpotifyApi api = new SpotifyApi();
                SpotifyService spotify = api.getService();

                Map<String, Object> map = new HashMap<>();
                map.put("country", "US");

                Tracks results = spotify.getArtistTopTrack(artistId, map);
                List<Track> topTracks = results.tracks;

                return topTracks;
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
                }

                ArrayList<ListOfTracks> trackStore = new ArrayList<ListOfTracks>();

                int rightSize = 0;
                if(topTracks.size() > 0) {
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
                            trackStore.add(new ListOfTracks(largeImage, image, trackName, albumName, previewUrl));
                            mSongArrayAdapter.add(new ListOfTracks(largeImage, image, trackName, albumName, previewUrl));
                        } else {
                            String largeImage = "http://lorempixel.com/640/640/cats/";
                            String image = "http://lorempixel.com/200/200/cats/";
                            String trackName = topTracks.get(i).name;
                            String albumName = topTracks.get(i).album.name;
                            String previewUrl = topTracks.get(i).preview_url;
                            trackStore.add(new ListOfTracks(largeImage, image, trackName, albumName, previewUrl));
                            mSongArrayAdapter.add(new ListOfTracks(largeImage, image, trackName, albumName, previewUrl));
                        }
                    }
                }
                trackList = trackStore;
            }
        }
    }
}