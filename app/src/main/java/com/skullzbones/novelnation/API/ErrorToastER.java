package com.skullzbones.novelnation.API;

import android.content.Context;
import android.widget.Toast;

import com.skullzbones.novelnation.R;

public class ErrorToastER {
    public enum TOAST_MESSAGE { TOAST_ERROR_LOADING_EXPLORE, TOAST_ERROR_NEXT_CHAP_UNV, TOAST_ERROR_PREV_CHAP_UNV}

    public static void makeToast(Context context, TOAST_MESSAGE toast_message){
        if(toast_message==TOAST_MESSAGE.TOAST_ERROR_LOADING_EXPLORE){
            Toast.makeText(context,context.getResources().getString(R.string.load_frag_failed),Toast.LENGTH_LONG).show();
        }
        if(toast_message==TOAST_MESSAGE.TOAST_ERROR_NEXT_CHAP_UNV){
            Toast.makeText(context,context.getResources().getString(R.string.next_chap_unav),Toast.LENGTH_LONG).show();
        }
        if(toast_message==TOAST_MESSAGE.TOAST_ERROR_PREV_CHAP_UNV){
            Toast.makeText(context,context.getResources().getString(R.string.prev_chap_unav),Toast.LENGTH_LONG).show();
        }
    }
}
