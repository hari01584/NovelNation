package com.skullzbones.novelnation.API;

import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.POJO.Chapter;

import java.util.ArrayList;
import java.util.Hashtable;

public interface CallBackBookItem {
    void fetchBookData(Hashtable<Integer, String> data, ArrayList<Chapter> chapters);
}