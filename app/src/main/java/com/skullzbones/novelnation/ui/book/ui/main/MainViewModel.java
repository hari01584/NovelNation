package com.skullzbones.novelnation.ui.book.ui.main;

import androidx.lifecycle.ViewModel;

import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.POJO.Chapter;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    boolean reverseList = false;
    Book book;
    String rating;
    String rank;
    String author;
    String genre;
    String type;
    String sypnosis;
    String progress;
    Boolean isCollapsed = true;

    ArrayList<Chapter> chapters;
    Integer current_chapter_cursor;
    Chapter current_chapter;
    // TODO: Implement the ViewModel
}