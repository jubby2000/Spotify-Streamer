package com.example.jacob.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jacob on 7/3/15.
 */
public class ListOfTracks implements Parcelable{
    String largeImage;
    String albumName;
    String trackName;
    String image;
    String previewUrl;
    String artistName;

    public ListOfTracks(String largeImage, String image, String trackName, String albumName, String previewUrl, String artistName)
    {
        this.largeImage = largeImage;
        this.image = image;
        this.trackName = trackName;
        this.albumName = albumName;
        this.previewUrl = previewUrl;
        this.artistName = artistName;
    }

    public String getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(String largeImage) {
        this.largeImage = largeImage;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    private ListOfTracks(Parcel in){
        largeImage = in.readString();
        image = in.readString();
        trackName = in.readString();
        albumName = in.readString();
        previewUrl = in.readString();
        artistName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString() { return largeImage + "--" + image + "--" + trackName + "--" +
            albumName + "--" + previewUrl + "--" + artistName; }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(largeImage);
        parcel.writeString(image);
        parcel.writeString(trackName);
        parcel.writeString(albumName);
        parcel.writeString(previewUrl);
        parcel.writeString(artistName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ListOfTracks> CREATOR = new Parcelable.Creator<ListOfTracks>() {
        @Override
        public ListOfTracks createFromParcel(Parcel parcel) {
            return new ListOfTracks(parcel);
        }

        @Override
        public ListOfTracks[] newArray(int i) {
            return new ListOfTracks[i];
        }

    };

}

