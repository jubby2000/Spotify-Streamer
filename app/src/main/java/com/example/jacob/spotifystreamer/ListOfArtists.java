package com.example.jacob.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacob on 7/3/15.
 */
public class ListOfArtists implements Parcelable{
    String artistName;
    //String albumName;
    //String songName;
    String artistId;
    String artistImage;
    String countryCode;
    String artistImageLarge;

    public ListOfArtists(String aImage, String aName, String aId, String aCountry, String aImageLarge)
    {
        this.artistImage = aImage;
        this.artistName = aName;
        this.artistId = aId;
        this.countryCode = aCountry;
        this.artistImageLarge = aImageLarge;
    }

    private ListOfArtists(Parcel in){
        artistImage = in.readString();
        artistName = in.readString();
        artistId = in.readString();
        //countryCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return artistName + "--" + artistImage + "--" + artistId; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(artistId);
        parcel.writeString(artistImage);
        parcel.writeString(artistName);
    }

    public final Parcelable.Creator<ListOfArtists> CREATOR = new Parcelable.Creator<ListOfArtists>() {
        @Override
        public ListOfArtists createFromParcel(Parcel parcel) {
            return new ListOfArtists(parcel);
        }

        @Override
        public ListOfArtists[] newArray(int i) {
            return new ListOfArtists[i];
        }

    };


}

