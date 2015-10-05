package com.example.jacob.spotifystreamer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jacob on 7/27/15.
 */
public class PlayerActivity extends ActionBarActivity {

    private final String LOG_TAG = PlayerActivity.class.getSimpleName();

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
        setContentView(R.layout.activity_player);

        String artistName = getIntent().getStringExtra("artistName");
        String albumName = getIntent().getStringExtra("albumName");
        String albumArtLarge = getIntent().getStringExtra("albumArtLarge");
        String trackName = getIntent().getStringExtra("trackName");
        String previewUrl = getIntent().getStringExtra("previewUrl");
        int currentPosition = getIntent().getIntExtra("currentPosition", 0);
        ArrayList trackList = getIntent().getParcelableArrayListExtra("trackList");
        String artistImageLarge = getIntent().getStringExtra("artistImageLarge");

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putString("artistName", artistName);
            args.putString("albumName", albumName);
            args.putString("albumArtLarge", albumArtLarge);
            args.putString("trackName", trackName);
            args.putString("previewUrl", previewUrl);
            args.putInt("currentPosition", currentPosition);
            args.putParcelableArrayList("trackList", trackList);
            args.putString("artistImageLarge", artistImageLarge);

            DialogFragment fragment = new PlayerDialogFragment();

            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.player, fragment)
                    .commit();
        }

//        Intent intent = getIntent();
//        String trackName = intent.getStringExtra("trackName");
//
//        String previewUrl = intent.getStringExtra("previewUrl");
//
//        Uri myUri = Uri.parse(previewUrl);
//        MediaPlayer mediaPlayer = new MediaPlayer();
//        try{
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setDataSource(this, myUri);
//            mediaPlayer.prepareAsync();
//        }catch(IOException error) {
//            Snackbar.make(this.findViewById(R.id.player),
//                    "Weird... I can't seem to find that track.",
//                    Snackbar.LENGTH_LONG).show();
//            return;
//        } catch (IllegalArgumentException e) {
//            Snackbar.make(this.findViewById(R.id.player),
//                    "Weird. I can't seem to find that track.",
//                    Snackbar.LENGTH_LONG).show();
//            return;
//        }
//        mediaPlayer.start();
//
        getSupportActionBar().setSubtitle(trackName);
//
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.player, new PlayerDialogFragment())
//                    .commit();
//        }
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

        public static class PlayerFragment extends Fragment {

            public static final String LOG_TAG = PlayerFragment.class.getSimpleName();

            //ListOfTracks[] trackViews = {};
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

            }

            @Override
            public void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
            }

            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup player,
                                     Bundle savedInstanceState) {


                View rootView = inflater.inflate(R.layout.player_layout, player, false);

                Intent intent = getActivity().getIntent();

                String artistName = intent.getStringExtra("artist");
                String albumName = intent.getStringExtra("album");
                String albumArtLarge = intent.getStringExtra("albumArtLarge");
                String trackName = intent.getStringExtra("trackName");


//                FetchTopSongsTask topSongsTask = new FetchTopSongsTask();
//
//                if (savedInstanceState == null) {
//                    topSongsTask.execute(artistId);
//                }

                TextView artistTextView = (TextView) rootView.findViewById(R.id.player_artist_name);
                artistTextView.setText(artistName);

                TextView albumTextView = (TextView) rootView.findViewById(R.id.player_album_name);
                albumTextView.setText(albumName);

                ImageView albumArtView = (ImageView) rootView.findViewById(R.id.player_album_art);
                try {
                    Picasso.with(getActivity())
                            .load(albumArtLarge)
                            .placeholder(R.drawable.solid_gray)
                            .into(albumArtView);
                } catch (IllegalArgumentException error) {
                    Picasso.with(getActivity())
                            .load("http://lorempixel.com/640/640/cats/")
                            .placeholder(R.drawable.solid_gray)
                            .into(albumArtView);
                }

                TextView trackTextView = (TextView) rootView.findViewById(R.id.player_track_name);
                trackTextView.setText(trackName);

                return rootView;
            }

//            private Runnable updateSeekBarTime = new Runnable() {
//                public void run() {
//                    //get current position
//                    timeElapsed = mediaPlayer.getCurrentPosition();
//                    //set seekbar progress
//                    seekbar.setProgress((int) timeElapsed);
//                    //set time remaing
//                    double timeRemaining = finalTime - timeElapsed;
//                    duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.
//                            toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS
//                            .toSeconds((long) timeRemaining) - TimeUnit.MINUTES
//                            .toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
//
//                    //repeat yourself that again in 100 miliseconds
//                    durationHandler.postDelayed(this, 100);
//                }
//            };
//
//                    // pause mp3 song
//            public void pause(View view) {
//                mediaPlayer.pause();
//            }
//
//                    // go forward at forwardTime seconds
//            public void forward(View view) {
//                //check if we can go forward at forwardTime seconds before song endes
//                if ((timeElapsed + forwardTime)  0) {
//                    timeElapsed = timeElapsed - backwardTime;
//
//                    //seek to the exact second of the track
//                    mediaPlayer.seekTo((int) timeElapsed);
//                }
//            }

//
//            public class FetchTopSongsTask extends AsyncTask<String, Void, List<Track>> {
//
//                private final String LOG_TAG = FetchTopSongsTask.class.getSimpleName();
//
//                public List<Track> getArtistTopTrack(@Path("id") String artistId, @QueryMap Map<String, Object> options) {
//                    SpotifyApi api = new SpotifyApi();
//                    SpotifyService spotify = api.getService();
//
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("country", "US");
//
//                    try {
//                        Tracks results = spotify.getArtistTopTrack(artistId, map);
//                        List<Track> topTracks = results.tracks;
//                        if (topTracks.size() == 0){
//                            Snackbar.make(getActivity().findViewById(R.id.track_list),
//                                    "I can't find any tracks for that artist.",
//                                    Snackbar.LENGTH_LONG).show();
//                            return null;
//                        }
//                        return topTracks;
//                    } catch (RetrofitError error) {
//                        Snackbar.make(getActivity().findViewById(R.id.track_list),
//                                "YO DAWG YOU NEED INTERNET TO INTERNET",
//                                Snackbar.LENGTH_LONG).show();
//                        return null;
//                    }
//
//                }
//
//                @Override
//                protected List<Track> doInBackground(String... query) {
//
//                    if (query.length == 0) {
//                        return null;
//                    }
//
//                    List<Track> tracks = getArtistTopTrack(query[0], null);
//
//                    return tracks;
//                }
//
//                @Override
//                protected void onPostExecute(List<Track> topTracks) {
//                    super.onPostExecute(topTracks);
//
//                    if (topTracks == null) {
//                        mSongArrayAdapter.clear();
//                    } else {
//
//                        ArrayList<ListOfTracks> trackStore = new ArrayList<ListOfTracks>();
//
//                        int rightSize = 0;
//                        if (topTracks.size() > 0) {
//                            for (int i = 0; i < topTracks.size(); i++) {
//                                if (!topTracks.get(i).album.images.isEmpty()) {
//                                    for (int i1 = 0; i1 < topTracks.get(i).album.images.size(); i1++) {
//                                        if (topTracks.get(i).album.images.get(i1).height < 200 &&
//                                                topTracks.get(i).album.images.get(i1).width < 200) {
//                                            rightSize = i1 - 1;
//                                        }
//                                    }
//
//                                    String largeImage = topTracks.get(i).album.images.get(0).url;
//                                    String image = topTracks.get(i).album.images.get(rightSize).url;
//                                    String trackName = topTracks.get(i).name;
//                                    String albumName = topTracks.get(i).album.name;
//                                    String previewUrl = topTracks.get(i).preview_url;
//                                    trackStore.add(new ListOfTracks(largeImage, image, trackName, albumName, previewUrl));
//                                    mSongArrayAdapter.add(new ListOfTracks(largeImage, image, trackName, albumName, previewUrl));
//                                } else {
//                                    String largeImage = "http://lorempixel.com/640/640/cats/";
//                                    String image = "http://lorempixel.com/200/200/cats/";
//                                    String trackName = topTracks.get(i).name;
//                                    String albumName = topTracks.get(i).album.name;
//                                    String previewUrl = topTracks.get(i).preview_url;
//                                    trackStore.add(new ListOfTracks(largeImage, image, trackName, albumName, previewUrl));
//                                    mSongArrayAdapter.add(new ListOfTracks(largeImage, image, trackName, albumName, previewUrl));
//                                }
//                            }
//                        }
//                        trackList = trackStore;
//                    }
//                }
//            }
//        }
    }

}
