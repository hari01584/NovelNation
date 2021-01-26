package com.skullzbones.novelnation.ui.book.ui.main;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.skullzbones.novelnation.API.API_CALL_LINKS;
import com.skullzbones.novelnation.API.CallBackBookItem;
import com.skullzbones.novelnation.API.CallBackChangToReadMode;
import com.skullzbones.novelnation.API.CallBackChapterDataParse;
import com.skullzbones.novelnation.API.CallBackGetBookID;
import com.skullzbones.novelnation.API.Utility;
import com.skullzbones.novelnation.MainActivity;
import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.POJO.Chapter;
import com.skullzbones.novelnation.POJO.ChapterAdapter;
import com.skullzbones.novelnation.R;
import com.skullzbones.novelnation.ui.book.bookDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;

import at.blogc.android.views.ExpandableTextView;

import static com.skullzbones.novelnation.Utility.Utility.transitReadingFragment;


public class BookReaderFragment extends Fragment {

    private MainViewModel mViewModel;
    private ImageView imageView;
    private TextView  titleHead;
    private TextView rating;
    private TextView rank;
    private TextView author;
    private TextView genre;
    private ExpandableTextView summary;
    private TextView status;
    private RecyclerView chapterList;
    private ImageButton imageButton;
    private LinearLayoutManager lm;

    public static BookReaderFragment newInstance() {
        return new BookReaderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);
        imageView = v.findViewById(R.id.book_picture);
        titleHead = v.findViewById(R.id.titleHeader);
        rank = v.findViewById(R.id.rank);
        author = v.findViewById(R.id.author);
        status = v.findViewById(R.id.status);
        rating = v.findViewById(R.id.rating);
        genre = v.findViewById(R.id.genre);
        summary = v.findViewById(R.id.bio);
        chapterList = v.findViewById(R.id.chapter_list_recyclerview);
        imageButton = v.findViewById(R.id.imageButton);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel

        //TODO: DEBUG CODE REMOVE THIS
       /*mViewModel.book = new Book(
                4856,
                "Reincarnation of strongest sword god",
                "0/0",
                "https://boxnovel.com/novel/omniscient-readers-viewpoint-boxnovel/",
                "https://boxnovel.com/wp-content/uploads/2018/09/omniscient-readers-viewpoint-193x278.jpg",
                "4.7",
                null);
*/

        lm = new LinearLayoutManager(getContext());
        lm.setStackFromEnd(mViewModel.reverseList);
        lm.setReverseLayout(mViewModel.reverseList);
        chapterList.setHasFixedSize(true);
        chapterList.setLayoutManager(lm);

        if(mViewModel.book==null){
            Book book = (Book) getActivity().getIntent().getSerializableExtra("MyClass");
            mViewModel.book = book;
            API_CALL_LINKS.getUniqIDfromLink(getContext(),mViewModel.book, new CallBackGetBookID() {
                @Override
                public void execute(Integer uid) {
                    mViewModel.book.uid = uid;
                    setViewPortsFetchBooks();
                }
            });
        }

        else{
            parseSetDataField();
        }

        //setViewPortsFetchBooks();
    }

    private void setViewPortsFetchBooks() {
        bookDetail.showProgressBar();
        API_CALL_LINKS.parseBookData(getContext(), mViewModel.book, (data, chapters) -> {
                mViewModel.author = data.get(API_CALL_LINKS.BOOK_AUTHOR);
                mViewModel.rating = data.get(API_CALL_LINKS.BOOK_RATE);
                mViewModel.rank = data.get(API_CALL_LINKS.BOOK_RANK);
                mViewModel.genre = data.get(API_CALL_LINKS.BOOK_GENRE);
                mViewModel.sypnosis = data.get(API_CALL_LINKS.BOOK_SUMMARY);
                mViewModel.type = data.get(API_CALL_LINKS.BOOK_TYPE);
                mViewModel.chapters = new ArrayList<>(chapters);
                parseSetDataField();
        });

    }
    ChapterAdapter adapter;

    private void parseSetDataField() {
        bookDetail.hideProgressBar();
        Picasso.get().load(mViewModel.book.imageUrl)
                .fit()
                .into(imageView);
        titleHead.setText(mViewModel.book.bookTitle);
        titleHead.setText(mViewModel.book.bookTitle);
        author.setText(mViewModel.author);
        rating.setText(mViewModel.rating);
        rank.setText(mViewModel.rank);
        genre.setText(mViewModel.genre);
        summary.setText(mViewModel.sypnosis);
        adapter = new ChapterAdapter(getContext(), mViewModel.chapters, new CallBackChangToReadMode() {
            @Override
            public void execute(int adapPos) {
                mViewModel.current_chapter_cursor = adapPos;
                FragmentManager rc= getActivity().getSupportFragmentManager();
                bookDetail.showProgressBar();
                transitReadingFragment(getContext(),getActivity().getSupportFragmentManager(),mViewModel.chapters.get(mViewModel.current_chapter_cursor).chapter_link);
            }
        });
        chapterList.setAdapter(adapter);

        for(int i=0;i<mViewModel.chapters.size();i++){
            if(mViewModel.chapters.get(i).isRead){
                lm.scrollToPosition(i-1);
            }
        }
        /*for(Chapter c: mViewModel.chapters){
            if(c.isRead){
               lm.scrollToPosition(mViewModel.chapters.indexOf(c));
               break;
            }
        }*/
/*
        if(mViewModel.reverseList){
            adapter.reverseList();
        }
*/

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.reverseList =  !mViewModel.reverseList;
                lm.setStackFromEnd(mViewModel.reverseList);
                lm.setReverseLayout(mViewModel.reverseList);
                //adapter.reverseList();
            }
        });

        summary.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (summary.isExpanded())
                {
                    summary.collapse();
                }
                else
                {
                    summary.expand();
                }
            }
        });
    }

    @Override
    public void onResume() {
        ((Runnable) () -> {
            if(adapter!=null) {
                mViewModel.chapters.clear();
                mViewModel.chapters.addAll(new ArrayList<>(MainActivity.appDatabase.chapterDao().getChaptersOfID(mViewModel.book.uid)));
                adapter.notifyDataSetChanged();
            }
        }).run();
        super.onResume();
    }

}