package com.example.jacob.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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

    private ArrayList<ListOfArtists> artistList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null || !savedInstanceState.containsKey("artists")) {
            artistList = new ArrayList<ListOfArtists>();
        } else {
            artistList = savedInstanceState.getParcelableArrayList("artists");
        }

        setHasOptionsMenu(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (artistList != null) {
            outState.putParcelableArrayList("artists", artistList);
        }
            super.onSaveInstanceState(outState);
    }


    public ArtistFragment() {
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

                    artistTask.execute(searchText);

                    return true;
                }
                return false;
            }
        });

        mArtistAdapter = new ArtistArrayAdapter(getActivity(), artistList);

        ListView listView = (ListView) rootView.findViewById(R.id.artist_list);
        listView.setAdapter(mArtistAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String artist = mArtistAdapter.getItem(i).artistName;
                String country = mArtistAdapter.getItem(i).countryCode;
                String artistId = mArtistAdapter.getItem(i).artistId;

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("artist", artist)
                        .putExtra("artistId", artistId)
                        .putExtra("country", country);
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

            /*
            for(Artist artist : artists) {
                artistList.add(new ListOfArtists(artist.images.toString(), artist.name, artist.id, "US"));
            }
            */

            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);

            ArrayList<ListOfArtists> artistStore = new ArrayList<ListOfArtists>();

            if (artists != null) {
                mArtistAdapter.clear();
            }

            int rightSize = 0;
            if(artists.size() > 0) {
                for (int i = 0; i < artists.size(); i++) {
                    if (!artists.get(i).images.isEmpty()) {
                        for (int i1 = 0; i1 < artists.get(i).images.size(); i1++) {
                            if (artists.get(i).images.get(i1).height < 200 &&
                                    artists.get(i).images.get(i1).width < 200) {
                                rightSize = i1 - 1;
                            }
                        }

                        String image = artists.get(i).images.get(rightSize).url;
                        Artist artist = artists.get(i);
                        String artistId = artists.get(i).id;
                        String country = "US";
                        artistStore.add(new ListOfArtists(image, artist.name, artistId, country));
                        mArtistAdapter.add(new ListOfArtists(image, artist.name, artistId, country));
                    } else {
                        Artist artist = artists.get(i);
                        String image = "http://lorempixel.com/200/200/cats/";
                        String artistId = artists.get(i).id;
                        String country = "US";
                        artistStore.add(new ListOfArtists(image, artist.name, artistId, country));
                        mArtistAdapter.add(new ListOfArtists(image, artist.name, artistId, country));
                    }
                    }
                }
            artistList = artistStore;
            }
        }
    }
