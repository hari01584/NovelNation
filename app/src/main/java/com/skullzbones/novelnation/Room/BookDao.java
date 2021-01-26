package com.skullzbones.novelnation.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.skullzbones.novelnation.POJO.Book;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BookDao {

    @Query("SELECT * FROM book ORDER BY timestamp DESC")
    List<Book> getBookLibrary();

    @Query("SELECT uid FROM book WHERE bookTitle is :str")
    Integer titletoId(String str);

    @Query("SELECT * FROM book WHERE uid is :id")
    Book getFromID(int id);


    @Query("UPDATE book SET timestamp=:timestamp WHERE uid IS :id")
    void updateTimestamp(int timestamp, int id);

    @Insert
    void insertAll(Book... users);

    @Insert
    void insert(Book book);


    @Delete
    void delete(Book user);
}