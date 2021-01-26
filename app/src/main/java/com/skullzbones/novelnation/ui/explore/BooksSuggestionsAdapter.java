package com.skullzbones.novelnation.ui.explore;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skullzbones.novelnation.POJO.Book;
import com.skullzbones.novelnation.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.skullzbones.novelnation.API.Utility.startBookActivity;

/**
 * Created by mancj on 27.01.17.
 */

public class BooksSuggestionsAdapter extends SuggestionsAdapter<Book, BooksSuggestionsAdapter.SuggestionHolder> {
    Resources res;
    public BooksSuggestionsAdapter(LayoutInflater inflater) {
        super(inflater);
    }

    @Override
    public int getSingleViewHeight() {
        return 80;
    }

    public Book getBookItemFrompos(int pos){
        return suggestions.get(pos);
    }


    public void addAllEntry(List<Book> books){
        suggestions.addAll(books);
        notifyDataSetChanged();
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_suggestion_cell, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindViewHolder(SuggestionHolder holder, int position) {
        res = holder.itemView.getContext().getResources();
        super.onBindViewHolder(holder, position);
    }

    @Override
    public void onBindSuggestionHolder(Book suggestion, SuggestionHolder holder, int position) {
        Picasso.get().load(suggestion.imageUrl)
                .fit()
                .into(holder.image);

        holder.title.setText(suggestion.bookTitle);
        holder.subtitle.setText(res.getString(R.string.rating_suggestion_format,suggestion.rating));
    }

    /**
     * <b>Override to customize functionality</b>
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p>
     * <p>This method is usually implemented by {@link androidx.recyclerview.widget.RecyclerView.Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if(term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    for (Book item: suggestions_clone)
                      //  if(item.bookTitle.toLowerCase().contains(term.toLowerCase()))
                            suggestions.add(item);
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<Book>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class SuggestionHolder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected TextView subtitle;
        protected ImageView image;

        public SuggestionHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            image = itemView.findViewById(R.id.frame_sugges);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    suggestions.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startBookActivity(v.getContext(),suggestions.get(getAdapterPosition()));
                }
            });
        }
    }

}