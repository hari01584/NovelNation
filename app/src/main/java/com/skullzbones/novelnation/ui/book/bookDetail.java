package com.skullzbones.novelnation.ui.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.skullzbones.novelnation.R;
import com.skullzbones.novelnation.ui.book.ui.main.BookReaderFragment;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class bookDetail extends AppCompatActivity {

    private static SmoothProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_reader_activity);

        progressBar = findViewById(R.id.book_activity_progress);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BookReaderFragment.newInstance())
                    .commitNow();
        }
    }

    public static void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                //that means your stack is empty and you want to close activity
                finish();
            } else {
                hideProgressBar();
                getSupportFragmentManager().popBackStackImmediate();
            }

    }
}