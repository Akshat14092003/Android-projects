package com.example.quotes;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class FavoriteQuotesActivity extends AppCompatActivity {

    private RecyclerView favoriteQuotesRecyclerView;
    private SharedPreferences sharedPreferences;
    private static final String FAVORITES = "favorites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_quotes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        favoriteQuotesRecyclerView = findViewById(R.id.favoriteQuotesRecyclerView);
        sharedPreferences = getSharedPreferences(FAVORITES, MODE_PRIVATE);

        loadFavoriteQuotes();
    }

    private void loadFavoriteQuotes() {
        Map<String, ?> favoritesMap = sharedPreferences.getAll();
        ArrayList<String> favoriteQuotesList = new ArrayList<>(favoritesMap.keySet());

        FavoriteQuotesAdapter adapter = new FavoriteQuotesAdapter(favoriteQuotesList, this);
        favoriteQuotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteQuotesRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
