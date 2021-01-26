package com.skullzbones.novelnation.POJO;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;

@Entity
public class Book implements Serializable {


    @PrimaryKey
    public Integer uid;
    @ColumnInfo
    public String bookTitle;
    @ColumnInfo
    public String bookProgress;
    @ColumnInfo
    public String Url;
    @ColumnInfo
    public String imageUrl;
    @ColumnInfo
    public String rating;
    @ColumnInfo
    public String color;
    @ColumnInfo
    public int timestamp;

    public Book(@Nullable Integer uid, String bookTitle, String bookProgress, String Url, String imageUrl, @Nullable String rating, @Nullable  String color)
    {
        this.uid=uid;
        this.bookTitle=bookTitle;
        this.bookProgress=bookProgress;
        this.Url=Url;
        this.imageUrl=imageUrl;
        this.rating=rating;
        this.color=color;
        timestamp = (int) (new Date().getTime()/1000);
    }

    @Ignore
    public void updateTimeStamp(){
        timestamp = (int) (new Date().getTime()/1000);
    }

    @NonNull
    @Override
    public String toString() {
        return "\n"+uid+"\n"+bookTitle+"\n"+bookProgress+"\n"+Url+"\n"+imageUrl+"\n"+rating+"\n"+color+"\n";
    }
}