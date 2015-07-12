package com.example.jacob.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static class DetailFragment extends Fragment {

        public static final String LOG_TAG = DetailFragment.class.getSimpleName();

        ListofTracks[] trackViews = {};

        private SongArrayAdapter mSongArrayAdapter;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.track_list_layout, container, false);

            mSongArrayAdapter = new SongArrayAdapter(getActivity(), new ArrayList<ListofTracks>());

            ListView listView = (ListView) rootView.findViewById(R.id.track_list);
            listView.setAdapter(mSongArrayAdapter);

            //if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            //    mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
            //    ((TextView) rootView.findViewById(R.id.album_and_song)).setText(mForecastStr);
            //}

            Intent intent = getActivity().getIntent();
            //String artistName = intent.getStringExtra("artist");
            String artistId = intent.getStringExtra("artistId");
            //String country = intent.getStringExtra("country"); //Probably unnecessary

            FetchTopSongsTask topSongsTask = new FetchTopSongsTask();

            topSongsTask.execute(artistId);

            return rootView;
        }

        public List<Track> getArtistTopTrack(@Path("id") String artistId, @QueryMap Map<String, Object> options){
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            Map<String, Object> map = new HashMap<>();
            map.put("country", Locale.getDefault().getCountry());

            Tracks results = spotify.getArtistTopTrack(artistId, map);
            List<Track> topTracks = results.tracks;

            return topTracks;
        }

        public class FetchTopSongsTask extends AsyncTask<String, Void, List<Track>> {

            private final String LOG_TAG = FetchTopSongsTask.class.getSimpleName();

            @Override
            protected List<Track> doInBackground(String... query) {

                if (query.length == 0) {
                    return null;
                }

                //Intent intent = getActivity().getIntent();
                //String artistName = intent.getStringExtra("artist");
                //String artistId = intent.getStringExtra("artistId");
                //String country = intent.getStringExtra("country"); //Probably unnecessary

                List<Track> topTracks = getArtistTopTrack(query[0], null);

                /*
                String searchString = query[0];

                SpotifyApi api = new SpotifyApi();
                SpotifyService service = api.getService();

                ArtistsPager results = service.searchArtists(searchString);
                List<Artist> artists = results.artists.items;
                return artists;
                */
                return topTracks;
            }

            @Override
            protected void onPostExecute(List<Track> topTracks) {
                super.onPostExecute(topTracks);

                if (topTracks != null) {
                    mSongArrayAdapter.clear();
                }

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
                            mSongArrayAdapter.add(new ListofTracks(largeImage, image, trackName, albumName, previewUrl));
                        } else {
                            String largeImage = "http://www.schofieldstone.com/img/pictemp.gif";
                            String image = "http://www.schofieldstone.com/img/pictemp.gif";
                            String trackName = topTracks.get(i).name;
                            String albumName = topTracks.get(i).album.name;
                            String previewUrl = topTracks.get(i).preview_url;
                            mSongArrayAdapter.add(new ListofTracks(largeImage, image, trackName, albumName, previewUrl));
                        }
                    }
                }
            }
        }

        /*FOR SHARING
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);

            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Fetch and store ShareActionProvider
            ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat
                    .getActionProvider(menuItem);

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            } else {
                Log.d(LOG_TAG, "Share action provider is null?");
            }
        }

        private Intent createShareForecastIntent() {
            // Create the share Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }
        */
    }
}