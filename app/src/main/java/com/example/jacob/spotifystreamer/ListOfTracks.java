package com.example.jacob.spotifystreamer;

/**
 * Created by jacob on 7/3/15.
 */
public class ListofTracks {
    String largeImage;
    String albumName;
    String trackName;
    String image;
    String previewUrl;

    public ListofTracks(String largeImage, String image, String trackName, String albumName, String previewUrl)
    {
        this.largeImage = largeImage;
        this.image = image;
        this.trackName = trackName;
        this.albumName = albumName;
        this.previewUrl = previewUrl;
    }

}

