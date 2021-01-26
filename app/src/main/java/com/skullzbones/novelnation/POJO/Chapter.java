package com.skullzbones.novelnation.POJO;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {"chapter_link"},unique = true)})
public class Chapter {

    @PrimaryKey(autoGenerate = true)
    public int record_index;

    @ColumnInfo
    public int book_id;

    @NonNull
    public String chapter_link;
    @ColumnInfo
    public String chapter_text;
    @ColumnInfo
    public String date_string;
    @ColumnInfo
    public String content;
    @ColumnInfo
    public boolean isRead;

    public Chapter(int book_id,String chapter_link, String chapter_text, String date_string,String content,boolean isRead){
        this.book_id=book_id;
        this.chapter_link=chapter_link;
        this.chapter_text=chapter_text;
        this.date_string=date_string;
        this.content=content;
        this.isRead = isRead;
    }
}
