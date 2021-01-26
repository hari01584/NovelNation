package com.skullzbones.novelnation.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.skullzbones.novelnation.API.API_CALL_LINKS;
import com.skullzbones.novelnation.API.CallBackChapterDataParse;
import com.skullzbones.novelnation.POJO.Chapter;
import com.skullzbones.novelnation.R;
import com.skullzbones.novelnation.ui.book.ui.main.ChapterReadingFragment;
import com.skullzbones.novelnation.ui.book.ui.main.MainViewModel;

import java.util.ArrayList;

public class Utility {
    public static final String KEY_CHAPTER_STRING = "transfer_data";


    public void generateSettingsCallers(Context context){

    }

    public static void transitReadingFragment(Context context, FragmentManager supportFragmentManager, String url) {
        API_CALL_LINKS.parseChapterData(context, url, new CallBackChapterDataParse() {
            @Override
            public void execute(Chapter chapter) {
                supportFragmentManager.popBackStack();

                Bundle bundle=new Bundle();
                bundle.putString(KEY_CHAPTER_STRING, chapter.chapter_link);
                ChapterReadingFragment c = ChapterReadingFragment.newInstance();
                c.setArguments(bundle);
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                transaction.replace(R.id.container, c);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }


    public static int getPreferenceTextChapterSize(Context baseContext){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(baseContext);
        int size =  Integer.parseInt(SP.getString("key_gallery_name","18"));
       // float size = multiplier*baseContext.getResources().getDimension(R.dimen.text_content_size);
        return size;
    }

    public static int getPreferenceTextFont(Context context) {
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        String font =  SP.getString("key_upload_quality","font/cinzelregular.ttf");
        if(font.equals("cinzelregular")){
            return R.font.cinzelregular;
        }
        else if(font.equals("yuseimagicreg")){
            return R.font.yuseimagicreg;
        }
        else if(font.equals("cormorantregular")){
            return R.font.cormorantregular;
        }
        else if(font.equals("hachimarupopreg")){
            return R.font.hachimarupopreg;
        }

        return R.font.yuseimagicreg;
    }

}