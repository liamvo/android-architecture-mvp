package com.liveteamvn.archmvp.data.local.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

/**
 * Created by liam on 10/10/2017.
 */
@Entity(tableName = "feed")
public class FeedEntity {

    @SerializedName("title")
    private String title;

    @NotNull
    @PrimaryKey
    @SerializedName("href")
    private String href = "";

    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("source")
    private String source;
    @SerializedName("dateTime")
    private String dateTime;
    @SerializedName("shortDescription")
    private String shortDescription;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotNull
    public String getHref() {
        return href;
    }

    public void setHref(@NotNull String href) {
        this.href = href;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

}
