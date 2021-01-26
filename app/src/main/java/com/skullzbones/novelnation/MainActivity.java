package com.skullzbones.novelnation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.widget.PopupMenuCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.skullzbones.novelnation.Room.AppDatabase;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {
    SmoothBottomBar bottomBar;
    NavController navController;
    public static AppDatabase appDatabase;
    public static SharedPreferences sharedPreferences;
    private static SmoothProgressBar dialogLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }*/
        dialogLoad = findViewById(R.id.progressDialog);
        hideProgressBar();

        bottomBar = findViewById(R.id.bottomBar);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_main_frag);
        navController = navHostFragment.getNavController();

        Menu meu = new PopupMenu(this,null).getMenu();
        getMenuInflater().inflate(R.menu.bottom_main_menu,meu);
        bottomBar.setupWithNavController(meu,navController);

        appDatabase = AppDatabase.getAppDatabase(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public static void showProgressBar(){
         dialogLoad.setVisibility(View.VISIBLE);
    }

    public static void hideProgressBar(){
         dialogLoad.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideProgressBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressBar();
    }

    /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Menu meu = new PopupMenu(this,null).getMenu();
        getMenuInflater().inflate(R.menu.bottom_main_menu,meu);
        bottomBar.setupWithNavController(meu,navController);
        return true;
    }*/
}