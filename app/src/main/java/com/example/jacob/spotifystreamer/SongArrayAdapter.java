package com.example.jacob.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jacob on 7/3/15.
 */

public class SongArrayAdapter extends ArrayAdapter<ListOfTracks> {
        private static final String LOG_TAG = SongArrayAdapter.class.getSimpleName();
        /**
         * This is our own custom constructor (it doesn't mirror a superclass constructor).
         * The context is used to inflate the layout file, and the List is the data we want
         * to populate into the lists
         *
         * @param context        The current context. Used to inflate the layout file.
         * @param trackViews A List of AndroidFlavor objects to display in a list
         */

        public SongArrayAdapter(Context context, ArrayList<ListOfTracks> trackViews) {
            // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
            // the second argument is used when the ArrayAdapter is populating a single TextView.
            // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
            // going to use this second argument, so it can be any value. Here, we used 0.
            super(context, 0, trackViews);
            //for (int i = 0; i < spotifyArtists.size(); i++) {
            //    artistViews.add(i, (new ListOfArtists((spotifyArtists.get(i).imageUrl),
            //            spotifyArtists.get(i).artistName)));
            //}
            //return artistViews;


        }

        /**
         * Provides a view for an AdapterView (ListView, GridView, etc.)
         *
         * @param position    The AdapterView position that is requesting a view
         * @param convertView The recycled view to populate.
         *                    (search online for "android view recycling" to learn more)
         * @param parent The parent ViewGroup that is used for inflation.
         * @return The View for the position in the AdapterView.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
            ListOfTracks trackViews = getItem(position);

            // Adapters recycle views to AdapterViews.
            // If this is a new View object we're getting, then inflate the layout.
            // If not, this view already has the layout inflated from a previous call to getView,
            // and we modify the View widgets as usual.
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.track_view_layout, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.album_image);
            try {
                Picasso.with(getContext())
                        .load(trackViews.image)
                        .into(imageView);
            } catch (IllegalArgumentException error) {
                Picasso.with(getContext())
                        .load("http://lorempixel.com/200/200/cats/")
                        .into(imageView);
            }

            TextView trackTextView = (TextView) convertView.findViewById(R.id.song_name);
            trackTextView.setText(trackViews.trackName);

            TextView albumTextView = (TextView) convertView.findViewById(R.id.album_name);
            albumTextView.setText(trackViews.albumName);

            return convertView;
        }
    }
