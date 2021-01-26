package com.skullzbones.novelnation.ui.book.ui.main;

import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.skullzbones.novelnation.POJO.Chapter;

public class ChapterReadingViewModel extends ViewModel {
    String title;
    String date;
    String content;
    Chapter current;
    String before;
    String after;
}
