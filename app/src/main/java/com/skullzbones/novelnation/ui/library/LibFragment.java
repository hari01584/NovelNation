package com.skullzbones.novelnation.ui.library;

import android.util.Log;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skullzbones.novelnation.MainActivity;
import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.POJO.BooksAdapter;
import com.skullzbones.novelnation.POJO.ItemOffsetDecoration;
import com.skullzbones.novelnation.R;
import com.skullzbones.novelnation.Utility.Utility;
import com.skullzbones.novelnation.ui.book.bookDetail;

import java.util.ArrayList;

import static com.skullzbones.novelnation.API.Utility.startBookActivity;

public class LibFragment extends Fragment {

    private static final String TAG = "l/LibFragment";
    private  LibViewModel mViewModel;
    private RecyclerView mRecyclerView;
    public static LibFragment newInstance() {
        return new LibFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library, container, false);
        mRecyclerView = v.findViewById(R.id.recycler_book_library);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LibViewModel.class);
        // TODO: Use the ViewModel

        int mNoOfColumns = getResources().getInteger(R.integer.recycleview_books_no_columns);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), mNoOfColumns));
        mRecyclerView.addItemDecoration(itemDecoration);

        if(mViewModel.booksLibrary==null || mViewModel.booksLibrary.size()==0){
            initDatabaseCollection();
        }
        else{
            setupDataForAdapterS();
        }
        
    }

    private void initDatabaseCollection() {
        new Runnable() {
            @Override
            public void run() {
                mViewModel.booksLibrary = new ArrayList<>(MainActivity.appDatabase.bookDao().getBookLibrary());
                for(int i=0; i<mViewModel.booksLibrary.size(); i++){
                    int bookId = mViewModel.booksLibrary.get(i).uid;
                    int read = MainActivity.appDatabase.chapterDao().countChaptersRead(bookId);
                    int total = MainActivity.appDatabase.chapterDao().countChaptersTotal(bookId);
                    mViewModel.booksLibrary.get(i).bookProgress = String.format("%d/%d", read, total);
                }
                setupDataForAdapterS();
            }
        }.run();
    }


    private void setupDataForAdapterS() {
        BooksAdapter adp01 = new BooksAdapter(getContext(), mViewModel.booksLibrary);
        mRecyclerView.setAdapter(adp01);
        adp01.setClickListener(new BooksAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, Book book) {
                startBookActivity(getContext(),book);
            }
        });

        if(mViewModel.booksLibrary.size()==0){
            Toast.makeText(getContext(),getResources().getString(R.string.first_explore),Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onResume() {
        ((Runnable) () -> {
            if(mRecyclerView!=null) {
                initDatabaseCollection();
            }
        }).run();
        super.onResume();
    }


}