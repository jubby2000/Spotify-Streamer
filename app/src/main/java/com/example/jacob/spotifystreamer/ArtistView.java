package com.example.jacob.spotifystreamer;

/**
 * Created by jacob on 7/3/15.
 */
public class ArtistView {
    String artistName;
    //String albumName;
    //String songName;
    String image; // drawable reference id

    public ArtistView(String image, String aName)
    {
        this.image = image;
        this.artistName = aName;
    }

}

