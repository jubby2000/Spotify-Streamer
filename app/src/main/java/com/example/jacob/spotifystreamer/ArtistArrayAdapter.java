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

public class ArtistArrayAdapter extends ArrayAdapter<ListOfArtists> {
        private static final String LOG_TAG = ArtistArrayAdapter.class.getSimpleName();
        /**
         * This is our own custom constructor (it doesn't mirror a superclass constructor).
         * The context is used to inflate the layout file, and the List is the data we want
         * to populate into the lists
         *
         * @param context        The current context. Used to inflate the layout file.
         * @param artistViews A List of AndroidFlavor objects to display in a list
         */

        public ArtistArrayAdapter(Context context, ArrayList<ListOfArtists> artistViews) {
            // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
            // the second argument is used when the ArrayAdapter is populating a single TextView.
            // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
            // going to use this second argument, so it can be any value. Here, we used 0.
            super(context, 0, artistViews);
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
            ListOfArtists artistViews = getItem(position);

            // Adapters recycle views to AdapterViews.
            // If this is a new View object we're getting, then inflate the layout.
            // If not, this view already has the layout inflated from a previous call to getView,
            // and we modify the View widgets as usual.
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.artist_layout, parent, false);
            }

            //ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_icon);
            //iconView.setImageResource(androidFlavor.image);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.artist_image);
            try {
                Picasso.with(getContext())
                        .load(artistViews.artistImage)
                        .into(imageView);
            } catch (IllegalArgumentException error) {
                Picasso.with(getContext())
                        .load("http://lorempixel.com/200/200/cats/")
                        .into(imageView);
            }


            TextView artistNameView = (TextView) convertView.findViewById(R.id.artist_name);
            artistNameView.setText(artistViews.artistName);

            return convertView;
        }
    }
