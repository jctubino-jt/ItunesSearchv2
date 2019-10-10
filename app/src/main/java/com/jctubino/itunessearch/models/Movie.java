package com.jctubino.itunessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @PrimaryKey
    @NonNull
    private int trackId;

    @ColumnInfo(name = "track_name")
    //Needed for the home page
    private String trackName;

    @ColumnInfo(name = "track_genre")
    private String primaryGenreName;

    @ColumnInfo(name = "track_short_description")
    private String shortDescription;

    @ColumnInfo(name = "track_price")
    private float trackPrice;

    @ColumnInfo(name = "track_currency")
    private String currency;

    @ColumnInfo(name = "track_image")
    private String artworkUrl100;

    //Needed for the details page

    @ColumnInfo(name = "track_preview_url")
    private String previewUrl;

    @ColumnInfo(name = "track_long_description")
    private String longDescription;

    @ColumnInfo(name = "track_time")
    private int trackTimeMillis;    //This needs conversion to Hour

    @ColumnInfo(name = "track_advisory_rating")
    private String contentAdvisoryRating;

    @ColumnInfo(name = "track_release_date")
    private String releaseDate;  // This needs conversion to Date

    @ColumnInfo(name = "track_timestamp")
    private int timestamp;


    public Movie(int trackId, String trackName, String primaryGenreName, String shortDescription, float trackPrice, String currency, String artworkUrl100,
                 String previewUrl, String longDescription, int trackTimeMillis, String contentAdvisoryRating, String releaseDate, int timestamp) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.primaryGenreName = primaryGenreName;
        this.shortDescription = shortDescription;
        this.trackPrice = trackPrice;
        this.currency = currency;
        this.artworkUrl100 = artworkUrl100;
        this.previewUrl = previewUrl;
        this.longDescription = longDescription;
        this.trackTimeMillis = trackTimeMillis;
        this.contentAdvisoryRating = contentAdvisoryRating;
        this.releaseDate = releaseDate;
        this.timestamp = timestamp;
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
        timestamp = in.readInt();
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
                "trackId=" + trackId +
                ", trackName='" + trackName + '\'' +
                ", primaryGenreName='" + primaryGenreName + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", trackPrice=" + trackPrice +
                ", currency='" + currency + '\'' +
                ", artworkUrl100='" + artworkUrl100 + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", trackTimeMillis=" + trackTimeMillis +
                ", contentAdvisoryRating='" + contentAdvisoryRating + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", timestamp=" + timestamp +
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
        parcel.writeInt(timestamp);
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
