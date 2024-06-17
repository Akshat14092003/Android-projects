package com.example.quotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView quoteTextView;
    private Button shareButton, favoriteButton;
    private String currentQuote;
    private SharedPreferences sharedPreferences;
    private static final String FAVORITES = "favorites";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quoteTextView = findViewById(R.id.quoteTextView);
        shareButton = findViewById(R.id.shareButton);
        favoriteButton = findViewById(R.id.favoriteButton);
        Button viewFavoritesButton = findViewById(R.id.viewFavoritesButton);

        sharedPreferences = getSharedPreferences(FAVORITES, MODE_PRIVATE);


        displayRandomQuote();


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQuote();
            }
        });


        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFavorites();
            }
        });


        viewFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteQuotesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayRandomQuote() {
        List<String> quotes = Arrays.asList(
                "The best way to predict the future is to create it.",
                "Success is not final, failure is not fatal: It is the courage to continue that counts.",
                "Don't watch the clock; do what it does. Keep going.",
                "Act as if what you do makes a difference. It does.",
                "The only limit to our realization of tomorrow is our doubts of today.",
                "All our dreams can come true, if we have the courage to pursue them.",
                "Don't sit down and wait for the opportunities to come. Get up and make them.",
                "If you don't like the road you're walking, start paving another one.",
                "When you have a dream, you've got to grab it and never let go."
        );

        Random random = new Random();
        currentQuote = quotes.get(random.nextInt(quotes.size()));
        quoteTextView.setText(currentQuote);
        quoteTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
    }

    private void shareQuote() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, currentQuote);
        startActivity(Intent.createChooser(shareIntent, "Share Quote via"));
    }

    private void saveToFavorites() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(currentQuote, currentQuote);
        editor.apply();
        Toast.makeText(MainActivity.this, "Saved to favorites", Toast.LENGTH_SHORT).show();
    }
}
