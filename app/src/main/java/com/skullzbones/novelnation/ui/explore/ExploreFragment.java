package com.skullzbones.novelnation.ui.explore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skullzbones.novelnation.API.API_CALL_LINKS;
import com.skullzbones.novelnation.API.CallBackSearchData;
import com.skullzbones.novelnation.API.CallBacks;
import com.skullzbones.novelnation.API.Utility;
import com.skullzbones.novelnation.MainActivity;
import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.POJO.BooksAdapter;
import com.skullzbones.novelnation.POJO.ItemOffsetDecoration;
import com.skullzbones.novelnation.R;
import com.skullzbones.novelnation.ui.book.bookDetail;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import me.ibrahimsn.lib.SmoothBottomBar;

import static com.skullzbones.novelnation.API.Utility.startBookActivity;

public class ExploreFragment extends Fragment implements BooksAdapter.ItemClickListener {

    private ExploreViewModel mViewModel;
    private RecyclerView recyclerVCate1;
    private RecyclerView recyclerVCate2;
    private RecyclerView recyclerVCate3;
    private RecyclerView recyclerVCate4;
    private RecyclerView recyclerVCate5;
    private RecyclerView recyclerVCate6;
    private MaterialSearchBar searchBar;
    private List<Book> lastSearches;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, container, false);

        recyclerVCate1 = v.findViewById(R.id.books_cate1);
        recyclerVCate2 = v.findViewById(R.id.books_cate2);
        recyclerVCate3 = v.findViewById(R.id.books_cate3);
        recyclerVCate4 = v.findViewById(R.id.books_cate4);
        recyclerVCate5 = v.findViewById(R.id.books_cate5);
        recyclerVCate6 = v.findViewById(R.id.books_cate6);
        searchBar = v.findViewById(R.id.searchBar);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ExploreViewModel.class);
        // TODO: Use the ViewModel

        int mNoOfColumns = getResources().getInteger(R.integer.recycleview_books_no_columns);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);

        recyclerVCate1.setLayoutManager(new GridLayoutManager(getContext(), mNoOfColumns));
        recyclerVCate1.addItemDecoration(itemDecoration);

        recyclerVCate2.setLayoutManager(new GridLayoutManager(getContext(), mNoOfColumns));
        recyclerVCate2.addItemDecoration(itemDecoration);

        recyclerVCate3.setLayoutManager(new GridLayoutManager(getContext(), mNoOfColumns));
        recyclerVCate3.addItemDecoration(itemDecoration);

        recyclerVCate4.setLayoutManager(new GridLayoutManager(getContext(), mNoOfColumns));
        recyclerVCate4.addItemDecoration(itemDecoration);

        recyclerVCate5.setLayoutManager(new GridLayoutManager(getContext(), mNoOfColumns));
        recyclerVCate5.addItemDecoration(itemDecoration);

        recyclerVCate6.setLayoutManager(new GridLayoutManager(getContext(), mNoOfColumns));
        recyclerVCate6.addItemDecoration(itemDecoration);

        if(mViewModel.list01.size()==0 || mViewModel.list02.size()==0){
            initDataCollection();
        }
        else{
            setupDataForAdapters();
        }
        setupSearchBar();

    }

    private void setupSearchBar() {
        //restore last queries from disk
        LayoutInflater inflater = (LayoutInflater) getActivity().getLayoutInflater();
        BooksSuggestionsAdapter booksSuggestionsAdapter = new BooksSuggestionsAdapter(inflater);
        lastSearches = Utility.loadSearchSuggestionFromDisk(getContext());
        if(lastSearches==null || lastSearches.size() < 2){
            lastSearches = new ArrayList<>();
            lastSearches.add(Utility.sample_book);
            lastSearches.add(Utility.sample_book);
            lastSearches.add(Utility.sample_book);
            lastSearches.add(Utility.sample_book);
            lastSearches.add(Utility.sample_book);
        }

        booksSuggestionsAdapter.setSuggestions(lastSearches);

        searchBar.setCustomSuggestionAdapter(booksSuggestionsAdapter);
        searchBar.setHint(getResources().getText(R.string.find_your_books_here));
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && (charSequence.length() % 5) == 0) {
                    API_CALL_LINKS.parseBookSearchData(getContext(), charSequence.toString(), new CallBackSearchData() {
                        @Override
                        public void execute(ArrayList<Book> ar) {
                            for(Book b:ar){
                                booksSuggestionsAdapter.addSuggestion(b);
                                Log.i("TAGGGG",b.bookTitle);
                            }
                            booksSuggestionsAdapter.getFilter().filter(charSequence.toString());
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                booksSuggestionsAdapter.getFilter().filter(s.toString());
            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(enabled) searchBar.setHint(getResources().getText(R.string.long_press_to_delete));
                else if(!enabled)  searchBar.setHint(getResources().getText(R.string.find_your_books_here));
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                Toast.makeText(getContext(),getResources().getString(R.string.type_auto_to_getlist),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void setupDataForAdapters() {
        initRecycler01();
        initRecycler02();
        initRecycler03();
        initRecycler04();
        initRecycler05();
        initRecycler06();
    }

    private void initRecycler01(){
        BooksAdapter adp01 = new BooksAdapter(getContext(), mViewModel.list01);
        recyclerVCate1.setAdapter(adp01);
        adp01.setClickListener(this);
    }

    private void initRecycler02(){
        BooksAdapter adp01 = new BooksAdapter(getContext(), mViewModel.list02);
        recyclerVCate2.setAdapter(adp01);
        adp01.setClickListener(this);
    }

    private void initRecycler03(){
        BooksAdapter adp01 = new BooksAdapter(getContext(), mViewModel.list03);
        recyclerVCate3.setAdapter(adp01);
        adp01.setClickListener(this);
    }

    private void initRecycler04(){
        BooksAdapter adp01 = new BooksAdapter(getContext(), mViewModel.list04);
        recyclerVCate4.setAdapter(adp01);
        adp01.setClickListener(this);
    }

    private void initRecycler05(){
        BooksAdapter adp01 = new BooksAdapter(getContext(), mViewModel.list05);
        recyclerVCate5.setAdapter(adp01);
        adp01.setClickListener(this);
    }

    private void initRecycler06(){
        BooksAdapter adp01 = new BooksAdapter(getContext(), mViewModel.list06);
        recyclerVCate6.setAdapter(adp01);
        adp01.setClickListener(this);
        MainActivity.hideProgressBar();
    }


    private void initDataCollection() {
        MainActivity.showProgressBar();
        //mViewModel.new_list = new ArrayList<>();
       // mViewModel.popular_list = new ArrayList<>();

        API_CALL_LINKS.parseCategoriesData(getContext(), API_CALL_LINKS.order_Trending,(new CallBacks() {
            @Override
            public void execute(ArrayList<Book> ar) {
                    mViewModel.list01 = new ArrayList<Book>(ar);
                    initRecycler01();
            }
        }));

        API_CALL_LINKS.parseCategoriesData(getContext(), API_CALL_LINKS.order_Views,(new CallBacks() {
            @Override
            public void execute(ArrayList<Book> ar) {
                mViewModel.list02 = new ArrayList<Book>(ar);
                initRecycler02();
            }
        }));


        API_CALL_LINKS.parseCategoriesData(getContext(), API_CALL_LINKS.order_Rating,(new CallBacks() {
            @Override
            public void execute(ArrayList<Book> ar) {
                mViewModel.list03 = new ArrayList<Book>(ar);
                initRecycler03();
            }
        }));



        API_CALL_LINKS.parseCategoriesData(getContext(), API_CALL_LINKS.order_Latest,(new CallBacks() {
            @Override
            public void execute(ArrayList<Book> ar) {
                mViewModel.list04 = new ArrayList<Book>(ar);
                initRecycler04();
            }
        }));



        API_CALL_LINKS.parseCategoriesData(getContext(), API_CALL_LINKS.order_Alphabet,(new CallBacks() {
            @Override
            public void execute(ArrayList<Book> ar) {
                mViewModel.list05 = new ArrayList<Book>(ar);
                initRecycler05();
            }
        }));


        API_CALL_LINKS.parseCategoriesData(getContext(), API_CALL_LINKS.order_New,(new CallBacks() {
            @Override
            public void execute(ArrayList<Book> ar) {
                MainActivity.hideProgressBar();
                mViewModel.list06 = new ArrayList<Book>(ar);
                initRecycler06();
            }
        }));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onItemClick(View view, Book book) {
        MainActivity.showProgressBar();
        startBookActivity(getContext(),book);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        //save last queries to disk
        Utility.saveSearchSuggestionToDisk(getContext(),searchBar.getLastSuggestions());
    }
}