package com.jctubino.itunessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    //Needed for the home page
    private String trackName;
    private String primaryGenreName;
    private String shortDescription;
    private float trackPrice;
    private String currency;
    private String artworkUrl100;

    //Needed for the details page
    private int trackId;
    private String previewUrl;
    private String longDescription;
    private int trackTimeMillis;    //This needs conversion to Hour
    private String contentAdvisoryRating;
    private String releaseDate;  // This needs conversion to Date


    public Movie(String trackName, String primaryGenreName, String shortDescription,
                 float trackPrice, String currency, String artworkUrl100, int trackId,
                 String previewUrl, String longDescription, int trackTimeMillis,
                 String contentAdvisoryRating, String releaseDate) {
        this.trackName = trackName;
        this.primaryGenreName = primaryGenreName;
        this.shortDescription = shortDescription;
        this.trackPrice = trackPrice;
        this.currency = currency;
        this.artworkUrl100 = artworkUrl100;
        this.trackId = trackId;
        this.previewUrl = previewUrl;
        this.longDescription = longDescription;
        this.trackTimeMillis = trackTimeMillis;
        this.contentAdvisoryRating = contentAdvisoryRating;
        this.releaseDate = releaseDate;
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        trackName = in.readString();
        primaryGenreName = in.readString();
        shortDescription = in.readString();
        trackPrice = in.readFloat();
        currency = in.readString();
        artworkUrl100 = in.readString();
        trackId = in.readInt();
        previewUrl = in.readString();
        longDescription = in.readString();
        trackTimeMillis = in.readInt();
        contentAdvisoryRating = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public void setPrimaryGenreName(String primaryGenreName) {
        this.primaryGenreName = primaryGenreName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public float getTrackPrice() {
        return trackPrice;
    }

    public void setTrackPrice(float trackPrice) {
        this.trackPrice = trackPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getArtworkUrl100() {
        artworkUrl100 = artworkUrl100.replaceAll("100x100","600x600");
        return artworkUrl100;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public int getTrackTimeMillis() {
        return trackTimeMillis;
    }

    public void setTrackTimeMillis(int trackTimeMillis) {
        this.trackTimeMillis = trackTimeMillis;
    }

    public String getContentAdvisoryRating() {
        return contentAdvisoryRating;
    }

    public void setContentAdvisoryRating(String contentAdvisoryRating) {
        this.contentAdvisoryRating = contentAdvisoryRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "trackName='" + trackName + '\'' +
                ", primaryGenreName='" + primaryGenreName + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", trackPrice=" + trackPrice +
                ", currency='" + currency + '\'' +
                ", artworkUrl100='" + artworkUrl100 + '\'' +
                ", trackId=" + trackId +
                ", previewUrl='" + previewUrl + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", trackTimeMillis=" + trackTimeMillis +
                ", contentAdvisoryRating='" + contentAdvisoryRating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(trackName);
        parcel.writeString(primaryGenreName);
        parcel.writeString(shortDescription);
        parcel.writeFloat(trackPrice);
        parcel.writeString(currency);
        parcel.writeString(artworkUrl100);
        parcel.writeInt(trackId);
        parcel.writeString(previewUrl);
        parcel.writeString(longDescription);
        parcel.writeInt(trackTimeMillis);
        parcel.writeString(contentAdvisoryRating);
        parcel.writeString(releaseDate);
    }
}
