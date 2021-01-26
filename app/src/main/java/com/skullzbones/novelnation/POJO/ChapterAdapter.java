package com.skullzbones.novelnation.POJO;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.skullzbones.novelnation.API.CallBackChangToReadMode;
import com.skullzbones.novelnation.R;
import com.skullzbones.novelnation.Utility.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Belal on 10/18/2017.
 */


public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {


    private final CallBackChangToReadMode callBackChangToReadMode;
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the Chapters in a list
    private ArrayList<Chapter> ChapterList;

    //getting the context and Chapter list with constructor
    public ChapterAdapter(Context mCtx, ArrayList<Chapter> ChapterList, CallBackChangToReadMode callBackChangToReadMode) {
        this.mCtx = mCtx;
        this.ChapterList = ChapterList;
        this.callBackChangToReadMode = callBackChangToReadMode;
    }

    @Override
    public ChapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_chapter_cell, null);

        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChapterViewHolder holder, int position) {
        //getting the Chapter of the specified position
        Chapter Chapter = ChapterList.get(position);

        holder.textViewTitle.setText(Chapter.chapter_text);
        holder.textViewShortDesc.setText(Chapter.date_string);

        if(Chapter.isRead){
            holder.textViewTitle.append(" ✔");
        }

    }


    @Override
    public int getItemCount() {
        return ChapterList.size();
    }

    public void reverseList() {
        Collections.reverse(ChapterList);
        notifyDataSetChanged();
    }


    class ChapterViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewShortDesc;

        public ChapterViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBackChangToReadMode.execute(getAdapterPosition());
                }
            });

        }
    }
}