package com.skullzbones.novelnation.ui.library;

import android.widget.Adapter;

import androidx.lifecycle.ViewModel;

import com.skullzbones.novelnation.POJO.Book;

import java.util.ArrayList;

public class LibViewModel extends ViewModel {
    ArrayList<Book> booksLibrary;
    Adapter adapter;
}