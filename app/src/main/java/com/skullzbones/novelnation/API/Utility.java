package com.skullzbones.novelnation.API;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skullzbones.novelnation.MainActivity;
import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.ui.book.bookDetail;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Utility {
    private static final String SUGGESTION_SAVE = "save-suggestion-data";
    public static Book sample_book = new Book(0,"Nothing Here So Far..","0/0","",null,"NA",null);

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 65; // You can vary the value held by the scalingFactor
        // variable. The smaller it is the more no. of columns you can display, and the
        // larger the value the less no. of columns will be calculated. It is the scaling
        // factor to tweak to your needs.
        int columnCount = (int) (dpWidth / scalingFactor-0.5);
        return (columnCount>=2?columnCount:2); // if column no. is less than 2, we still display 2 columns
    }

    public static void saveSearchSuggestionToDisk(Context context,List<Book> lastSearches){
        SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonData = gson.toJson(lastSearches);
        editor.putString(SUGGESTION_SAVE, jsonData);

        editor.commit();
    }

    public static List<Book> loadSearchSuggestionFromDisk(Context context){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Book>>(){}.getType();
        List<Book> sKey = gson.fromJson(MainActivity.sharedPreferences.getString(SUGGESTION_SAVE,null), type);
        return sKey;
    }

    public static void startBookActivity(Context context, Book book) {
        Intent intent = new Intent(context, bookDetail.class);
        intent.putExtra("MyClass", book);
        context.startActivity(intent);
    }


    public static void updateTimestamp(int bookId){
        new Runnable() {
            @Override
            public void run() {
                int ts = (int) (new Date().getTime()/1000);
                MainActivity.appDatabase.bookDao().updateTimestamp(ts,bookId);
            }
        }.run();
    }
}
