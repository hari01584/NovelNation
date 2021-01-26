package com.skullzbones.novelnation.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.POJO.Chapter;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ChapterDao {
    @Query("SELECT * FROM chapter where book_id=:bookid")
    List<Chapter> getChaptersOfID(int bookid);

    @Query("SELECT * FROM chapter WHERE chapter_link is :link LIMIT 1")
    Chapter getChapterFromLink(String link);

    @Query("SELECT chapter_link FROM Chapter WHERE book_id is :bookId and record_index= (SELECT MIN(record_index) FROM Chapter WHERE record_index> (SELECT record_index FROM Chapter WHERE chapter_link is :currentLink))")
    String LeftLink(int bookId, String currentLink);

    @Query("SELECT chapter_link FROM Chapter WHERE book_id is :bookId and record_index= (SELECT MAX(record_index) FROM Chapter WHERE record_index< (SELECT record_index FROM Chapter WHERE chapter_link is :currentLink))")
    String RightLink(int bookId, String currentLink);


    @Query("SELECT COUNT(*) from chapter WHERE book_id is :bookid")
    Integer countChaptersTotal(int bookid);

    @Query("SELECT COUNT(*) from chapter WHERE book_id is :bookid AND isRead is 1")
    Integer countChaptersRead(int bookid);


    @Insert
    void insertAll(ArrayList<Chapter> chapters);

    @Insert
    void insert(Chapter chapter);

    @Update
    void update(Chapter chapter);

    @Delete
    void delete(Chapter chapter);
}
