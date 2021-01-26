package com.skullzbones.novelnation.POJO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skullzbones.novelnation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {

    private ArrayList<Book> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public BooksAdapter(Context context, ArrayList<Book> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_item_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookProgress.setText(mData.get(position).bookProgress);
        holder.bookTitle.setText(mData.get(position).bookTitle);
        Picasso.get()
                .load(mData.get(position).imageUrl)
                .resize(200,300)
                .into(holder.bookIcon);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bookTitle;
        TextView bookProgress;
        ImageView bookIcon;
        ViewHolder(View itemView) {
            super(itemView);
            bookProgress = itemView.findViewById(R.id.info_text);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookIcon = itemView.findViewById(R.id.myImageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, mData.get(getAdapterPosition()));
        }
    }

    // convenience method for getting data at click position
    Book getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, Book position);
    }
}