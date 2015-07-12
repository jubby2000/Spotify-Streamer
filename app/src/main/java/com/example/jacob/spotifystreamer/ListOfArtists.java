package com.example.jacob.spotifystreamer;

/**
 * Created by jacob on 7/3/15.
 */
public class ListOfArtists {
    String artistName;
    //String albumName;
    //String songName;
    String artistId;
    String artistImage;
    String countryCode;

    public ListOfArtists(String aImage, String aName, String aId, String aCountry)
    {
        this.artistImage = aImage;
        this.artistName = aName;
        this.artistId = aId;
        this.countryCode = aCountry;
    }

}

