package com.skullzbones.novelnation.ui.book.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.skullzbones.novelnation.API.API_CALL_LINKS;
import com.skullzbones.novelnation.API.CallBackChapterDataParse;
import com.skullzbones.novelnation.API.CallBackNext;
import com.skullzbones.novelnation.API.CallBackPrev;
import com.skullzbones.novelnation.API.ErrorToastER;
import com.skullzbones.novelnation.MainActivity;
import com.skullzbones.novelnation.POJO.Chapter;
import com.skullzbones.novelnation.R;
import com.skullzbones.novelnation.Utility.Utility;
import com.skullzbones.novelnation.ui.book.bookDetail;

import okhttp3.internal.Util;

public class ChapterReadingFragment extends Fragment {

    private static final long DURATION = 700;
    private ChapterReadingViewModel cViewModel;

    public static ChapterReadingFragment newInstance() {
        return new ChapterReadingFragment();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (enter) {
            return MoveAnimation.create(MoveAnimation.UP, enter, DURATION);
        } else {
            return CubeAnimation.create(CubeAnimation.UP, enter, DURATION);
        }
    }

    TextView titleHead;
    TextView dateStr;
    TextView content;
    ImageButton Lbutton;
    ImageButton Rbutton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_reading_chapters, container, false);
        titleHead = v.findViewById(R.id.chapter_title);
        dateStr = v.findViewById(R.id.date_update);
        content = v.findViewById(R.id.content);
        Lbutton = v.findViewById(R.id.left);
        Rbutton = v.findViewById(R.id.right);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String link=getArguments().getString(Utility.KEY_CHAPTER_STRING);
        if(link==null||link.isEmpty()){
            Toast.makeText(getContext(),getResources().getString(R.string.error_null_chapter),Toast.LENGTH_LONG).show();
            return;
        }
        cViewModel = ViewModelProviders.of(this).get(ChapterReadingViewModel.class);
        if(cViewModel.content==null){
            API_CALL_LINKS.parseChapterData(getContext(), link, new CallBackChapterDataParse() {
                @Override
                public void execute(Chapter chapter) {
                    cViewModel.title = chapter.chapter_text;
                    cViewModel.content = chapter.content;
                    cViewModel.date = chapter.date_string;
                    cViewModel.current = chapter;
                    initPopularizeData();
                }
            });
        }
        else{
            initPopularizeData();
        }

        Lbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API_CALL_LINKS.getPrevBookUrl(cViewModel.current.book_id, cViewModel.current.chapter_link, new CallBackPrev() {
                    @Override
                    public void execute(String prev) {
                        if(prev==null || prev.isEmpty()) {
                            ErrorToastER.makeToast(getContext(), ErrorToastER.TOAST_MESSAGE.TOAST_ERROR_PREV_CHAP_UNV);
                            return;
                        }
                        bookDetail.showProgressBar();
                        Utility.transitReadingFragment(getContext(),getActivity().getSupportFragmentManager(),prev);
                    }
                });
            }
        });

        Rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API_CALL_LINKS.getNextBookUrl(cViewModel.current.book_id, cViewModel.current.chapter_link, new CallBackNext() {
                    @Override
                    public void execute(String next) {
                        if(next==null || next.isEmpty()) {
                            ErrorToastER.makeToast(getContext(), ErrorToastER.TOAST_MESSAGE.TOAST_ERROR_NEXT_CHAP_UNV);
                            return;
                        }
                        bookDetail.showProgressBar();
                        Utility.transitReadingFragment(getContext(),getActivity().getSupportFragmentManager(),next);
                    }
                });
            }
        });
    }



    private void initPopularizeData() {
        bookDetail.hideProgressBar();
        titleHead.setText(cViewModel.title);
        dateStr.setText(cViewModel.date);
        content.setText(cViewModel.content);

        content.setTextSize(Utility.getPreferenceTextChapterSize(getContext()));
        Typeface face = ResourcesCompat.getFont(getContext(),Utility.getPreferenceTextFont(getContext()));
        content.setTypeface(face);

    }
}
