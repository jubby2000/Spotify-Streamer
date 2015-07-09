package com.example.jacob.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFragment extends Fragment {

    private final String LOG_TAG = ArtistFragment.class.getSimpleName();
    private ArtistArrayAdapter mArtistAdapter;

    ListOfArtists[] artistViews = {};

    ArrayList<String> artistList = new ArrayList<>();

    ArrayList<String> imageList = new ArrayList<>();

    public ArtistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final EditText editText = (EditText) rootView.findViewById(R.id.artist_search);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = "";
                    searchText = editText.getText().toString();

                    FetchArtistsTask artistTask = new FetchArtistsTask();
                    Toast.makeText(getActivity(), searchText, Toast.LENGTH_LONG).show();

                    artistTask.execute(searchText);
                    Log.i(LOG_TAG, artistList.toString());
                    return true;
                }
                return false;
            }
        });

        mArtistAdapter = new ArtistArrayAdapter(getActivity(), artistList, imageList, Arrays.asList(artistViews));

        ListView listView = (ListView) rootView.findViewById(R.id.artist_list);
        listView.setAdapter(mArtistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String artist = mArtistAdapter.getItem(i).toString();

// TODO More needs to be sent to the detail activity than just text, get top 10 songs based on artist chosen
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchArtistsTask extends AsyncTask<String, Void, List<Artist>> {

        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected List<Artist> doInBackground(String... query) {

            if (query.length == 0) {
                return null;
            }

            String searchString = query[0];

            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();

            ArtistsPager results = service.searchArtists(searchString);
            List<Artist> artists = results.artists.items;
            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);

            int rightSize = 0;
            Log.i("Artist", String.valueOf(artists.size()));
            if(artists.size() > 0) {
                //mArtistAdapter.clear();
                for (int i = 0; i < artists.size(); i++) {
                    if (!artists.get(i).images.isEmpty()) {
                        for (int i1 = 0; i1 < artists.get(i).images.size(); i1++) {
                            if (artists.get(i).images.get(i1).height < 200 &&
                                    artists.get(i).images.get(i1).width < 200) {
                                rightSize = i1 - 1;
                            }
                        }

                        String image = artists
                                .get(i)
                                .images
                                .get(rightSize)
                                .url;
                        Artist artist = artists.get(i);
                        artistList.add(i, artist.name);
                        imageList.add(i, image);

                        //Log.i(LOG_TAG, i + " " + image + " " + artist.name);
                    } else {
                        Artist artist = artists.get(i);
                        String image = "http://www.schofieldstone.com/img/pictemp.gif";
                        artistList.add(i, artist.name);
                        imageList.add(i, image);
                    }
                    }

                }
                //mArtistAdapter.notifyDataSetChanged();
            }
        }
    }


