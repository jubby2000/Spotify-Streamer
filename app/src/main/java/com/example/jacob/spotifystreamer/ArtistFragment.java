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

    ArtistView[] artistViews = new ArtistView[] {};

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

        mArtistAdapter = new ArtistArrayAdapter(this.getActivity(), Arrays.asList(artistViews));
/*
        mArtistAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.artist_list_item,
                R.id.artist_name,
                new ArrayList<String>());
*/
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
                    return true;
                }
                return false;
            }
        });

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

            for (int i = 0; i < artists.size(); i++) {
                    String image = artists.get(i).images.get(1).url;
                    Artist artist = artists.get(i);
                    Log.i(LOG_TAG, i + " " + image + artist.name);
            }

            return artists;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);

            Log.i("Artist", String.valueOf(artists.size()));
            if(artists.size() > 0) {
                mArtistAdapter.clear();
                for (int i = 0; i < artists.size(); i++) {
                        Artist artist = artists.get(i);
                        //mArtistAdapter.add(artist.name);
                        new ArtistView(artist.images.get(1).url, artist.name);
                        Log.i(LOG_TAG, i + " " + artist.name);

                }
                mArtistAdapter.notifyDataSetChanged();
            }

        }
    }
}

