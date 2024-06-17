package com.example.quotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;


public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.QuoteViewHolder> {

    private final List<String> favoriteQuotesList;
    private Context context;

    public FavoriteQuotesAdapter(List<String> favoriteQuotesList, Context context) {
        this.favoriteQuotesList = favoriteQuotesList;
        this.context = context;
    }

    @NonNull
    @Override
    public QuoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new QuoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteViewHolder holder, int position) {
        String quote = favoriteQuotesList.get(position);
        holder.quoteTextView.setText(quote);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Quote");
                builder.setMessage("Are you sure you want to delete this quote from favorites?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteFromFavorites(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteQuotesList.size();
    }

    private void deleteFromFavorites(int position) {
        String quoteToDelete = favoriteQuotesList.get(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences("favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(quoteToDelete);
        editor.apply();

        favoriteQuotesList.remove(position);
        notifyItemRemoved(position);
    }

    static class QuoteViewHolder extends RecyclerView.ViewHolder {
        TextView quoteTextView;

        public QuoteViewHolder(@NonNull View itemView) {
            super(itemView);
            quoteTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}

